package com.hfad.mymp3.Service;

import android.app.ActivityManager;
import android.content.Context;

/*
위 클래스는 서비스에 관련된 유틸리티를 제공하는 클래스고 대부분 static 함수로 이루어 가야합니다.
 */
public class ServiceUtil {

    //현재 Mp3 서비스가 실행되고 있는지 확인하는 메서드 입니다.
    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass){
        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);      // 메니저를 받고
        for(ActivityManager.RunningServiceInfo service :manager.getRunningServices(Integer.MAX_VALUE)){     //실행하고 있는 서비스랑 같은지 확인
            if(serviceClass.getName().equals(service.service.getClassName())){          // 실행되고 있는 서비스들을 계속 받은뒤 이름이 같은 지 확인하여 true를 받환한다.
                return true;
            }
        }
        return false;
    }
}
