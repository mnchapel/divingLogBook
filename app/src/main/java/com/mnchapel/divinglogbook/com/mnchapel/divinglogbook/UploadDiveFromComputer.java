package com.mnchapel.divinglogbook.com.mnchapel.divinglogbook;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.os.AsyncTask;

import com.mnchapel.divinglogbook.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import static android.content.ContentValues.TAG;
import static java.lang.Thread.sleep;


public class UploadDiveFromComputer extends Fragment {

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private UsbDevice device;
    private TextView tvImportLogs;
    private UsbManager usbManager;
    private PendingIntent permissionIntent;
    private static String appTag = "divinglogbook";
    private UsbDeviceConnection usbConnection;
    private UsbEndpoint readEndpoint;
    private UsbEndpoint writeEndpoint;

    private static final byte HDR_DEVINFO_SPYDER    = 0x16;
    private static final byte HDR_DEVINFO_VYPER     = 0x24;
    private static final byte HDR_DEVINFO_BEGIN     = (HDR_DEVINFO_SPYDER);
    private static final byte HDR_DEVINFO_END       = (byte)(HDR_DEVINFO_VYPER+6);

    private static final int FTDI_REQUEST_RESET         = 0x00;
    private static final int FTDI_VALUE_RESET_RX        = 0x01;
    private static final int FTDI_VALUE_RESET_TX        = 0x02;
    private static final int FTDI_REQUEST_SET_BAUDRATE  = 0x03;
    private static final int FTDI_REQUEST_SET_DATA      = 0x04;
    private static final int FTDI_REQTYPE_WRITE         = 0x40;
    private static final int FTDI_VALUE_DATA_CONFIG     = 0x08; /* Parity None, 8 Data Bits and 1 Stop Bit */
    private static final int FTDI_VALUE_BAUDRATE        = 0x04E2; /* 2400 baudrate */
    private static final int FTDI_TIMEOUT_WRITE         = 5000;

    private static int SZ_PACKET = 32;



    public UploadDiveFromComputer() {
        // Required empty public constructor
    }



    private byte checksum_xor_uint8(byte data[], int size, byte init) {
        byte crc = init;
        for(int i=0; i<size; ++i)
            crc ^= data[i];
        return crc;
    }



    private void setBaudRate() {
        int result = usbConnection.controlTransfer(FTDI_REQTYPE_WRITE, FTDI_REQUEST_SET_BAUDRATE,
                                                   FTDI_VALUE_BAUDRATE,0, null,
                                                   0, FTDI_TIMEOUT_WRITE);

        Log.i(appTag, "setBaudRate: transfer " + result + " bytes");
    }



    private void setDataConfiguration() {

        // https://github.com/ysykhmd/usb-serial-for-xamarin-android/blob/master/UsbSerialForAndroid/Cp21xxSerialPort.cs
        int config_data_bits = 0;
        config_data_bits |= 0x0800; // 8 bits
        config_data_bits |= 0x0010; // odd parity
        config_data_bits |= 0; // one stop bit

        config_data_bits = (byte)0x08;

        int result = usbConnection.controlTransfer(FTDI_REQTYPE_WRITE, FTDI_REQUEST_SET_DATA, config_data_bits,0, null, 0, FTDI_TIMEOUT_WRITE);
        Log.i(appTag, "setDataConfiguration: transfer " + result + " bytes");
    }



    private void purgeBuffers(boolean read_buffer, boolean write_buffer) {
        int result;

        if(read_buffer) {
            result = usbConnection.controlTransfer(FTDI_REQTYPE_WRITE, FTDI_REQUEST_RESET, FTDI_VALUE_RESET_RX, 0, null, 0, FTDI_TIMEOUT_WRITE);
            Log.i(appTag, "purgeBuffers: read buffer " + result + " bytes");
        }

        if(write_buffer) {
            result = usbConnection.controlTransfer(FTDI_REQTYPE_WRITE, FTDI_REQUEST_RESET, FTDI_VALUE_RESET_TX, 0, null, 0, FTDI_TIMEOUT_WRITE);
            Log.i(appTag, "purgeBuffers: write buffer " + result + " bytes");
        }
    }



    private void suunto_vyper_transfer(byte command[], int csize, byte answer[], int asize, int size) {
        int result;

        // Send the command to the dive computer
        result = usbConnection.bulkTransfer(writeEndpoint, command, csize, 1000);
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(appTag, "suunto_vyper_transfer: send command, transfer " + result + " bytes");

        //Receive the answer of the dive computer
        result = usbConnection.bulkTransfer(readEndpoint, answer, asize, 1000);
        Log.i(appTag, "suunto_vyper_transfer: receive answer, transfer " + result + " bytes");

        // Verify the answer of the dive computer
        //boolean areEquals = Arrays.asList(command).subList(0,asize-size-1).equals(Arrays.asList(answer).subList(0,asize-size-1));
        //if(!areEquals) {
        if(command[0] != answer[0]) {
            Log.i(appTag, "suunto_vyper_transfer: Unexpected answer start byte(s).");
        }
        else {
            Log.i(appTag, "suunto_vyper_transfer: answer success");
        }
    }



    private void suunto_vyper_device_read(int address, byte data[], int size) {
        int nbytes = 0;
        while(nbytes < size) {
            // Calculate the package size
            int len = Math.min(size - nbytes, SZ_PACKET);

            // Read the package
            byte answer[] = new byte[SZ_PACKET+5];
            byte command[] = {(byte)0x05, (byte)((address>>8)&0xFF), (byte)((address)&0xFF), (byte)len, (byte)0};
            command[4] = checksum_xor_uint8(command, 4, (byte)0x00);

            suunto_vyper_transfer(command, command.length, answer, len+5, len);

            nbytes += len;
        }
    }



    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(device != null){

                            UsbInterface usbInterface = device.getInterface(0);
                            usbConnection = usbManager.openDevice(device);
                            readEndpoint = null; //usbInterface.getEndpoint(0);
                            writeEndpoint = null; //usbInterface.getEndpoint(1);

                            int nb_endpoint = usbInterface.getEndpointCount();
                            for(int i = 0; i<nb_endpoint; i++) {
                                if (usbInterface.getEndpoint(i).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                                    Log.i(appTag, "Bulk Endpoint");
                                    if (usbInterface.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_IN)
                                        readEndpoint = usbInterface.getEndpoint(i);
                                    else
                                        writeEndpoint = usbInterface.getEndpoint(i);
                                } else {
                                    Log.i(appTag, "Not Bulk");
                                }
                            }


                            if(usbConnection != null) {
                                Log.i(appTag, "Device opened");
                            } else {
                                Log.i(appTag, "Failed to open device");
                            }

                            boolean claimed  = usbConnection.claimInterface(usbInterface, true);
                            if(claimed) {
                                Log.i(appTag, "Interface claimed");
                            } else {
                                Log.i(appTag, "Failed to claim interface");
                            }

                            setBaudRate();
                            try {
                                sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            setDataConfiguration();
                            try {
                                sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            purgeBuffers(true, true);
                            try {
                                sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            usbConnection.controlTransfer(0x21, 0x22, 0x1, 0, null, 0, 0); // DTR

                            // Set Data Terminal Ready (DTR)
                            

                            byte header[] = new byte[HDR_DEVINFO_END - HDR_DEVINFO_BEGIN];
                            suunto_vyper_device_read(HDR_DEVINFO_BEGIN, header, HDR_DEVINFO_END - HDR_DEVINFO_BEGIN);

                            /*// READ THE FIRST PACKAGE
                            boolean init = true;

                            byte[] cmd = {init ? (byte)0x08 : (byte)0x09, (byte)0xA5, (byte)0x00};

                            // Change Checksum
                            byte crc = 0x00;
                            for(int i=0; i<2; i++)
                                crc ^= cmd[i];

                            cmd[2] = crc;

                            int result = usbConnection.bulkTransfer(writeEndpoint, cmd, 3, 5000);
                            Log.i(appTag, result+"");

                            byte[] answer = new byte[SZ_PACKET+3];
                            int a = answer.length;
                            result = usbConnection.bulkTransfer(readEndpoint, answer, answer.length, 5000);

                            // Verify the header of the package.
                            if (answer[0] != cmd[0]) {
                                Log.i(appTag, "Error in hearder cmd\n");
                            }
                            else if(answer[1] > SZ_PACKET) {
                                Log.i(appTag, "Error in hearder SZ_PACKET\n");
                            }
                            else {
                                Log.i(appTag, result+"");
                                //logs += answer;
                            }*/

                            usbConnection.close();
                        }
                    }
                    else {
                        Log.d(TAG, "permission denied for device " + device);
                    }
                }
            }
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // To register the broadcast receiver for usb permission
        permissionIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        getActivity().registerReceiver(mUsbReceiver, filter);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_dive_from_computer, container, false);

        tvImportLogs = (TextView) view.findViewById(R.id.textTest);

        usbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Log.i("divinglogbook", deviceList.size()+"");

        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while(deviceIterator.hasNext()){
            device = deviceIterator.next();
            Log.i("divinglogbook", device.getDeviceName());
            // To display the dialog that asks users for permission to connect to the device
            usbManager.requestPermission(device, permissionIntent);
        }

        return view;
    }




//    private class ImportTask extends AsyncTask<Void, Void, Void> {
//
//        private static final String TAG = "ImportTask";
//
//        private static final int FTDI_REQTYPE_READ  = 0xC0;
//        private static final int FTDI_REQTYPE_WRITE = 0x40;
//
//        private static final int FTDI_REQUEST_RESET          = 0x00; /* Reset the port */
//        private static final int FTDI_REQUEST_SET_MODEM_CTRL = 0x01; /* Set the modem control register */
//        private static final int FTDI_REQUEST_SET_FLOW_CTRL  = 0x02; /* Set flow control register */
//        private static final int FTDI_REQUEST_SET_BAUDRATE   = 0x03; /* Set baud rate */
//        private static final int FTDI_REQUEST_SET_DATA       = 0x04; /* Set the data characteristics of the port */
//
//        private static final int FTDI_VALUE_DATA_CONFIG = 0x08; /* Parity None, 8 Data Bits and 1 Stop Bit */
//        private static final int FTDI_VALUE_BAUDRATE    = 0x1A; /* corresponding to 115200 baudrate - the factor would be 3000000/115200 */
//
//        private static final int FTDI_VALUE_RESET_IO    = 0x00;
//        private static final int FTDI_VALUE_RESET_RX    = 0x01;
//        private static final int FTDI_VALUE_RESET_TX    = 0x02;
//
//        private static final int FTDI_TIMEOUT_READ  = 5000;
//        private static final int FTDI_TIMEOUT_WRITE = 5000;
//
//        private static final int FTDI_TIME_SLEEP = 300;
//
//        /**
//         * OSTC 3 Specific Commands
//         */
//        public static final int INIT       = 0xBB;
//        public static final int EXIT       = 0xFF;
//        public static final int READY      = 0x4D;
//        public static final int IDENTITY   = 0x69;
//        public static final int HEADER     = 0x61;
//        public static final int DIVE       = 0x66;
//
//
//        private byte[] readData = new byte[64];
//        private byte[] writeData = new byte[1];
//
//        private boolean forceClaim = true;
//
//        private UsbInterface usbInterface;
//        private UsbEndpoint readEndpoint;
//        private UsbEndpoint writeEndpoint;
//        private UsbDeviceConnection usbConnection;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            threadState = ThreadState.RUNNING;
//
//            usbInterface = device.getInterface(0);
//            readEndpoint = usbInterface.getEndpoint(0);
//            writeEndpoint = usbInterface.getEndpoint(1);
//            usbConnection = usbManager.openDevice(device);
//
//            if(usbConnection != null) {
//                Log.d(TAG, "Device opened");
//                logs += "Device opened\n";
//            } else {
//                Log.e(TAG, "Failed to open device");
//                logs += "Failed to open device\n";
//                this.cancel(true);
//            }
//            boolean claimed  = usbConnection.claimInterface(usbInterface, forceClaim);
//            if(claimed) {
//                Log.d(TAG, "Interface claimed");
//                logs += "Interface claimed\n";
//            } else {
//                Log.e(TAG, "Failed to claim interface");
//                logs += "Failed to claim interface\n";
//                this.cancel(true);
//            }
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            synchronized (this) {
//                setBaudRate();
//                setDataConfiguration();
//                purgeBuffers(true, true);
//                initialiseDevice();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void v) {
//            super.onPostExecute(v);
//            closeDevice();
//            tvImportLogs.setText(logs);
//            threadState = ThreadState.STOPPED;
//            importTask = new ImportTask();
//            logs += "\n\n";
//        }
//
//        @Override
//        protected void onCancelled(Void v) {
//            super.onCancelled(v);
//            if(usbConnection != null) {
//                closeDevice();
//            }
//            tvImportLogs.setText(logs);
//            threadState = ThreadState.STOPPED;
//            importTask = new ImportTask();
//            logs += "\n\n";
//        }
//
//        void closeDevice() {
//            byte[] exit = {(byte) EXIT};
//            int result = usbConnection.bulkTransfer(
//                    writeEndpoint,
//                    exit, 1,
//                    FTDI_TIMEOUT_WRITE);
//            if(result < 0) {
//                Log.e(TAG, "Failed to send EXIT command.");
//                logs += "Failed to send EXIT command\n";
//            } else {
//                Log.d(TAG, "EXIT command sent.");
//                logs += "EXIT command sent.\n";
//            }
//        }
//
//        void initialiseDevice() {
//            byte[] init = {(byte) INIT};
//            int result = usbConnection.bulkTransfer(
//                    writeEndpoint,
//                    init, 1,
//                    FTDI_TIMEOUT_WRITE);
//            if(result < 0) {
//                Log.e(TAG, "Failed to write INIT command.");
//                logs += "Failed to write INIT command.\n";
//                this.cancel(true);
//            } else {
//                Log.d(TAG, "INIT command written");
//                logs += "INIT command written.\n";
//                sleep();
//            }
//
//            byte[] echo = new byte[3];
//            result = usbConnection.bulkTransfer(
//                    readEndpoint,
//                    echo, 3,
//                    FTDI_TIMEOUT_READ);
//            if(result < 0) {
//                Log.e(TAG, "Failed to read echo of INIT.");
//                logs += "Failed to read echo of INIT.\n";
//                this.cancel(true);
//            } else {
//                Log.d(TAG, String.format("Echo of INIT read : %02x|%02x|%02x",
//                        echo[0], echo[1], echo[2]));
//                logs += String.format("Echo of INIT read : %02x|%02x|%02x\n",
//                        echo[0], echo[1], echo[2]);
//                sleep();
//            }
//
//            if(echo[0] != init[0]) {
//                Log.e(TAG, "Echo not same as INIT.");
//                logs += "Echo not same as INIT.\n";
//                this.cancel(true);
//            } else {
//                Log.d(TAG, "Echo verified.");
//                logs += "Echo verified.\n";
//            }
//
//        }
//
//        void setDataConfiguration() {
//            int result = usbConnection.controlTransfer(
//                    FTDI_REQTYPE_WRITE,
//                    FTDI_REQUEST_SET_DATA,
//                    FTDI_VALUE_DATA_CONFIG,
//                    0, null, 0,
//                    FTDI_TIMEOUT_WRITE);
//            if(result != 0) {
//                Log.e(TAG, "Failed to set the data configuration.");
//                logs += "Failed to set the data configuration.\n";
//                this.cancel(true);
//            } else {
//                Log.d(TAG, "Data configuration set");
//                logs += "Data configuration set.\n";
//                sleep();
//            }
//        }
//
//        void setBaudRate() {
//            int result = usbConnection.controlTransfer(
//                    FTDI_REQTYPE_WRITE,
//                    FTDI_REQUEST_SET_BAUDRATE,
//                    FTDI_VALUE_BAUDRATE,
//                    0, null, 0,
//                    FTDI_TIMEOUT_WRITE);
//            if(result != 0) {
//                Log.e(TAG, "Failed to set the baudrate.");
//                logs += "Failed to set the baudrate.\n";
//                this.cancel(true);
//            } else {
//                Log.d(TAG, "Baudrate set.");
//                logs += "Baudrate set.\n";
//                sleep();
//            }
//        }
//
//        private void purgeBuffers(boolean readBuffer, boolean writeBuffer) {
//            if(readBuffer) {
//                int result = usbConnection.controlTransfer(
//                        FTDI_REQTYPE_WRITE,
//                        FTDI_REQUEST_RESET,
//                        FTDI_VALUE_RESET_RX,
//                        0, null, 0,
//                        FTDI_TIMEOUT_WRITE);
//                if(result != 0) {
//                    Log.e(TAG, "Failed to purge read buffer.");
//                    logs += "Failed to purge read buffer.\n";
//                    this.cancel(true);
//                } else {
//                    Log.d(TAG, "Read buffer purged.");
//                    logs += "Read buffer purged.\n";
//                    sleep();
//                }
//            }
//            if(writeBuffer) {
//                int result = usbConnection.controlTransfer(
//                        FTDI_REQTYPE_WRITE,
//                        FTDI_REQUEST_RESET,
//                        FTDI_VALUE_RESET_TX,
//                        0, null, 0, FTDI_TIMEOUT_WRITE);
//                if(result != 0) {
//                    Log.e(TAG, "Failed to purge write buffer.");
//                    logs += "Failed to purge write buffer.\n";
//                    this.cancel(true);
//                } else {
//                    Log.d(TAG, "Write buffers purged.");
//                    logs += "Write buffers purged.\n";
//                    sleep();
//                }
//            }
//            sleep();
//        }
//        private void sleep() {
//            try {
//                new Thread().sleep(FTDI_TIME_SLEEP);
//            } catch (InterruptedException e) {}
//        }
//
//    }
}
