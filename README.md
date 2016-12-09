# IRModule Yofeng

Very simple library to connect to a special USB OTG IR module on Android.

The lib/d2xx.jar came with the hardware, this library is a simplification of the
work.

## Simple usage

There is only support for opening and sending data, it is assumed that the
recording of commands is done using another application.



```java
// *this* is an android.content.Context, for example, Activity
IRModuleYofeng mIRModule = new IRModuleYofeng(this);
if ( mIRModule.open() )
{
    mIRModule.write(0x40);
    // ...some time later...
    mIRModule.close();
}


```

Demo @ [IRModule Yofeng Demo](/aaronps/irmoduleyofeng-demo).