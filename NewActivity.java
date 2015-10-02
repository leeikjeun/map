package loca.android.org.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class NewActivity  extends FragmentActivity implements LocationListener {
    GoogleMap map;
    private LocationManager manager;
    String provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity);


        int googlePlayServiceResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(NewActivity.this);// googleplayservicesUtil.is그거 오류가 발생했는지 여부 판단
        if(googlePlayServiceResult != ConnectionResult.SUCCESS){//connetction은 구글플레이서비스에서 연결하는데 실패할 경우의 가능한 모든 코드를 넣은 기능 success는 성공했다는 표시 성공하지 않았을 경우를 비교
            GooglePlayServicesUtil.getErrorDialog(googlePlayServiceResult,this,0, new DialogInterface.OnCancelListener(){//oncancelListner는 취소시 호출되는 메소드
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    finish();
                }
            }).show();
        }else {
            manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//연결에 성공했을때 위치 정보를 받아옴
            Criteria criteria = new Criteria();//위치 제공자를 선택하기 위한 기준을 적용 나타내는 클래스
            provider = manager.getBestProvider(criteria,true);//가장 지정된 기준을 충족하는 공급자의 이름을 돌려줍니다.

            if(provider == null){
                new AlertDialog.Builder(NewActivity.this).setTitle("위치서비스 동위").setNeutralButton("이동", new DialogInterface.OnClickListener(){//alertdialog.builder는 기본경고 대화박스 만듬인데 안나오네;; ㅅㅂ
                    @Override//setTitle는 경고상자 제목 setNuetralButton은
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),0);//startActivityForResult는 새창 불러옴
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener(){//취소했을때의 함수
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        finish();//끝내다 라는 의미
                    }
                }).show();
            }else {
                manager.requestLocationUpdates(provider, 5, 5, NewActivity.this);// 기본 위치값 설정 provider 공급자 , 1 최소 업데이트 시간 간격, 위치 갱신 이동 간격, 지정된 locationlisner에게 위치갱신
                setUpMapIfNeeded();//
            }

            setUpMylocation();
            onMapReady(map);
        }

        Button button = (Button) findViewById(R.id.connet_internet);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=RmJHM02wwfo"));
                startActivity(intent);
            }
        });

    }

    public void onMapReady(GoogleMap map){
        String[][] loc = {
                {"46763745", "126.5733101", "혜주원", "1층" },
                {"44825717", "126.5533602", "춘강장애인근로센터", "현관앞" },
                {"44501937", "126.5605109", "제주특별자치도 제주의료원", "구급차" },
                {"4451282", "126.5601813", "제주특별자치도 제주의료원", "구급차안(무료진료차량)" },
                {"4539698", "126.5708737", "제주케어하우스", "1층로비" },
                {"45386652", "126.5709898", "제주장애인요양원", "2층로비" },
                {"44714666", "126.5598646", "제주인재개발원", "대강당" },
                {"45447603", "126.5609542", "제주대학교", "해양과학대학" },
                {"45114473", "126.5565505", "제주대학교 학생 생활관","학생생활관 1호관 식당앞 농협 CD기 옆" },
                {"45058416", "126.5570575", "제주대학교 학생 생활관", "학생생활관2호관 경비실옃로비" },
                {"4549206", "126.55541", "제주대학교 학생 생활관", "학생생활관3호관 로비" },
                {"45081633", "126.5573736", "제주대학교 학생 생활관", "학생생활관4호관 경비실앞" },
                {"45269375", "126.5608153", "제주대학교", "제2도서관" },
                {"45272667", "126.5596105", "제주대학교 수의과대학", "수의과대학" },
                {"4575153", "126.5637544", "제주대학교", "사회과학대학" },
                {"45249473", "126.5590376", "제주대학교 문화교류관", "문화교류원 2층 계단옆" },
                {"45551345", "126.5598438", "제주대학교", "교육대학교 학처" },
                {"45636959", "126.5597373", "제주대학교", "경상대학 1호관현관" },
                {"4527065", "126.5621893", "제주대학교 간호대학", "간호학과" },
                {"44173636", "126.5699472", "제주대학교", "도서관수서정리과" },
                {"45596353", "126.5621891", "제주대학교", "아라호" },
                {"45421459", "126.559689", "제주대학교", "외국어교육원 1층현관" },
                {"45431882", "126.5585753", "제주대학교", "의학전문대학원" },
                {"45317895", "126.5574947", "제주대학교", "인문대학 본관입구" },
                {"44184721", "126.5694143", "제주대학교", "평생교육원" },
                {"45704536", "126.5634421", "제주대학교", "생명과학대학" },
                {"44205218", "126.5700209", "제주국제대학교", "학생복지과" },
                {"44215424", "126.5695526", "제주국제대학교", "학생복지과" },
                {"44164665", "126.5700767", "제주국제대학교", "학생복지과" },
                {"4414851", "126.5689913", "제주국제대학교", "학생복지과" },
                {"44140068", "126.5693466", "제주국제대학교", "학생복지과" },
                {"4418276", "126.5702586", "제주국제대학교", "학생복지과" },
                {"440823", "126.5704571", "제주국제대학교", "학생복지과" },
                {"44143541", "126.5702606", "제주국제대학교", "학생복지과" },
                {"44162986", "126.5691358", "제주국제대학교", "학생복지과" },
                {"44190055", "126.5685483", "제주국제대학교", "학생복지과" },
                {"45548072", "126.578979", "영주고등학교", "중앙현관" },
                {"44013035", "126.5798019", "양지공원", "현관" },
                {"44025345", "126.5795432", "양지공원", "출입구" },
                {"44766902", "126.5571718", "소방방재본부", "교육실" },
                {"44501666", "126.5605099", "제주도립요양원", "1층 다목적실" },
                {"46723911", "126.5728334", "효사랑노인전문요양원", "정수기옆" },
                {"45509799", "126.564748", "제주대학교", "공과대학 중앙현관" },
                {"45535575", "126.5605034", "제주대학교", "공과대학 중앙현관" },
                {"45359334", "126.5602475", "제주대학교", "법학전문대학원" },
                {"45428686", "126.5610009", "제주대학교", "학생복지과" },
                {"45478345", "126.562796", "제주대학교", "자연과학대학행정실 중앙현관" },
                {"45550158", "126.562221", "제주대학교", "사범대학" },
                {"45557389", "126.5619544", "제주대학교", "경영사업단국재교류회관 2층" },
                {"45316748", "126.5580702", "제주대학교 예술디자인대학 음악학부", "음악관1층" },
                {"45590683", "126.559862", "제주대학교", "체육진흥센터" },
                {"45298811", "126.5602397", "제주대학교", "교육강의동 2층 기초교육원앞 " },
                {"45314147", "126.5558358", "아라뮤즈홀", "로비" } };

        for(int i=0; i<loc.length; i++){
            map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble("33."+loc[i][0]),Double.parseDouble(loc[i][1]))).title(loc[i][2]).snippet(loc[i][3]));
        }

    }



private LatLng mylocation;


    private void setUpMylocation(){
        map.setOnMyLocationChangeListener(myLocationChangeListener);//밑에서 적용된 위치를 지도에 나타냄
    }

Marker mMarker;
private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener(){
    @Override
    public void onMyLocationChange(Location location) {
        mylocation = new LatLng(location.getLatitude(),location.getLongitude());
        //mMarker = map.addMarker(new MarkerOptions().position(loc));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 16.0f));// 내위치 줌

    }
};

    protected void onActivityResul(int requestCode, int resultCode, Intent data){//위치설정 엑티비티 종료후
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                provider = manager.getBestProvider(criteria,true);
                if(provider == null){
                    finish();
                }else {// 사용자가 동의 했을경우
                    manager.requestLocationUpdates(provider,1L,2F,NewActivity.this);
                    Log.d("KTH", "117 LocationManager done");
                    setUpMapIfNeeded();
                }
                break;
        }

    }
    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.removeUpdates(this);//지정된 location에 대한 모든 위치정보업데이트 제거
    }

    private void setUpMapIfNeeded() {
        if (map == null) {
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps)).getMap();
            if (map != null) {
                setUpMap();//지도를 불러오고
            }
        }
    }

    private void setUpMap() {
        map.setMyLocationEnabled(true);//내 위치를 와 행동정보 동의 같은거라 생각하면 됨
        map.getMyLocation();//현재있는 위치를 반환 getmylocation은 곧 사라질 함수
    }

boolean locationTag = true;
    @Override
    public void onLocationChanged(Location location) {
        if(locationTag){//한번만 위치를 가져오기 위해서 tag를 주었습니다
            Log.d("myLog"  , "onLocationChanged: !!"  + "onLocationChanged!!");
            double lat =  location.getLatitude();//내 현재 위도를 알아내는 함수
            double lng = location.getLongitude();//내 현재 경도를 알아내는 함수

            Toast.makeText(NewActivity.this, "위도  : " + lat + " 경도: " + lng, Toast.LENGTH_SHORT).show();
            Log.d("myLog",lat+""+lng);
            locationTag=false;
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}





