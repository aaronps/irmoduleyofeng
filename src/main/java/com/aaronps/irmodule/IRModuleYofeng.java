package com.aaronps.irmodule;

import android.content.Context;
import android.util.Log;

import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;

/**
 * Created by krom on 2016-05-09.
 */

public class IRModuleYofeng {
    private static final String TAG = "IRModuleYofeng";
    D2xxManager mD2xxManager;
    Context mContext;
    FT_Device ft_device;
    int mAvailableDevices = 0;

    public IRModuleYofeng(Context context) {
        mContext = context;
        try
        {
            mD2xxManager = D2xxManager.getInstance(context);
        }
        catch (D2xxManager.D2xxException ex)
        {
            ex.printStackTrace();
        }

        // @todo does it need to do the SetupD2xxLibrary?
        SetupD2xxLibrary();

        // @todo register the intent and usb receiver so gets notified when the device is plugged in or out.

    }

    public boolean open() {
        if ( mAvailableDevices == 0 )
        {
            mAvailableDevices = mD2xxManager.createDeviceInfoList(mContext);
        }

        if ( mAvailableDevices == 0 )
        {
            Log.d(TAG, "open: There is not available devices");
            return false;
        }

        if ( ft_device == null )
        {
            // just open the first one.
            ft_device = mD2xxManager.openByIndex(mContext, 0);
        }

        if ( ft_device == null )
        {
            Log.d(TAG, "open: openByIndex returns null");
            return false;
        }

        if ( ! ft_device.isOpen() )
        {
            Log.d(TAG, "open: openByIndex didn't open the device");
            return false;
        }

        configure_port();

        // @todo create read thread

        return true;
    }

    public void close() {
        if ( ft_device != null && ft_device.isOpen() )
        {
            ft_device.close();
        }
        // XXX why not nulling? May be a bug here?
//        ft_device = null;
    }

    public boolean write(int code) {
        if ( ft_device != null && ft_device.isOpen() )
        {
            final byte[] buffer = new byte[]{(byte)(code&0xff)};
            ft_device.setLatencyTimer((byte)16);
            ft_device.write(buffer, 1);
            return true;
        }
        return false;
    }

    private void SetupD2xxLibrary () {
        // These magic values are from ftdi
        if(!mD2xxManager.setVIDPID(0x0403, 0xada1))
            Log.i("IRModuleYofeng","setVIDPID Error");

    }

    private boolean configure_port() {
        if ( ! ft_device.isOpen() ) return false;

        ft_device.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);
        ft_device.setBaudRate(9600);
        ft_device.setDataCharacteristics( D2xxManager.FT_DATA_BITS_8,
                                          D2xxManager.FT_STOP_BITS_1,
                                          D2xxManager.FT_PARITY_NONE );

        ft_device.setFlowControl( D2xxManager.FT_FLOW_NONE,
                                  (byte) 0x0b,
                                  (byte) 0x0d);
        return true;
    }

}
