package attila.toth.production.cardtrick.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import attila.toth.production.cardtrick.MainActivity;
import attila.toth.production.cardtrick.R;
import attila.toth.production.cardtrick.receiver.MyReceiver;


public class CardShowingService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private  Sensor accelerometerSensor;
    private Sensor gyroscopeSensor;

    private WindowManager windowManager;
    private View floatingView;
    private ImageView imageView;
    //private TextView tvFloatLat;
    //private TextView tvFloatLng;

    //Bundle extras;
    String resource;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /*
         * TODO showFloatingWindow fgv-t hívni és akkor megjelenik a kártyalap de csak akkor ha a szenzor elért egy gyorsulást
         */

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(CardShowingService.this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(CardShowingService.this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Bundle extras = intent.getExtras();
        resource = extras.getString(MainActivity.TAG);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        sensorManager.unregisterListener(CardShowingService.this);
        hideFloatingWindow();
        stopSelf();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float gyorsulasX = event.values[0];
        float gyorsulasY = event.values[1];
        float gyorsulasZ = event.values[2];

        double eredo = Math.sqrt(Math.pow(gyorsulasX,2) + Math.pow(gyorsulasY,2) + Math.pow(gyorsulasZ, 2));

        eredo = Math.abs(eredo - SensorManager.STANDARD_GRAVITY);

        if(eredo > 11){
            showFloatingWindow();
            // egy megjelenítés után le kell iratkozni meg véletlen rázás esetén több kártya is megjelenhet
            sensorManager.unregisterListener(CardShowingService.this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void showFloatingWindow() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        floatingView = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.card_layout, null);
        imageView = (ImageView) floatingView.findViewById(R.id.card_imageview);
        if(resource != null){
            if(resource.equals(getString(R.string.recycleblue))) {imageView.setImageResource(R.drawable.blue_card_back);}
            if(resource.equals(getString(R.string.recyclered))) {imageView.setImageResource(R.drawable.red_card_back);}
            if(resource.equals(getString(R.string.recyclegreen))) {imageView.setImageResource(R.drawable.green_card_back);}
            if(resource.equals(getString(R.string.recyclequater))) {imageView.setImageResource(R.drawable.quater_dollar_coin);}
            if(resource.equals(getString(R.string.recycleketszaz))) {imageView.setImageResource(R.drawable.ketszaz_forint);}
        }
        //tvFloatLat = (TextView) floatingView.findViewById(R.id.tvFloatLat);
        //tvFloatLng = (TextView) floatingView.findViewById(R.id.tvFloatLng);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 35;
        params.y = 0;

        windowManager.addView(floatingView, params);


        try {
            floatingView.setOnTouchListener(new View.OnTouchListener() {
                private WindowManager.LayoutParams paramsF = params;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = paramsF.x;
                            initialY = paramsF.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            Display mdisp = windowManager.getDefaultDisplay();
                            int maxX = mdisp.getWidth();
                            if(paramsF.x >= maxX-550 && paramsF.y <= 450){
                                hideFloatingWindow();
                                scheduleNotification(getNotification(), 20000);
                                //imageView.setImageDrawable(null);
                            }

                            paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                            paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                            //ez eltünteti a képet ha 1 pixelt is odébb mozdul

                            //imageView.setImageDrawable(null);
                            if (floatingView != null) {
                                windowManager.updateViewLayout(floatingView, paramsF);
                            }
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideFloatingWindow() {
        if (floatingView != null) {
            windowManager.removeView(floatingView);
            floatingView = null;
            //tvFloatLat = null;
            //tvFloatLng = null;
        }
    }

    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(getString(R.string.notification1));
        builder.setContentText(getString(R.string.notification2));
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        builder.setVibrate( new long[] {200,500,800,1200} );
        builder.setLights(Color.MAGENTA, 1500,1500);
        return builder.build();
    }

    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, MyReceiver.class);
        notificationIntent.putExtra(MyReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(MyReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
}
