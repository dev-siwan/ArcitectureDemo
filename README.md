# Android Architecture Demo
## 프로젝트 소개

간단하게 Feed(게시물)을 CRUD하는 프로젝트 입니다.
 
Android Jecpack Component를 이용하였으며, 각각의 Presnter Layer, Data Layer, Domain Layer를 모듈로 나눠 UI와 Data를 독립적인 구조를 적용하였습니다.

Network의 활용은 Firestore와 Firestorage를 이용하였습니다.
</br>
 ## 프로젝트 모듈 도식화
![Alt text](https://github.com/DeveloperKimsiwan/ArcitectureDemo/blob/master/img/DemoArchitecture.png?raw=true)
<br></br>
 * ####Presenter Layer
 Activity, Fragment, View 등 Android UI부분과 ViewModel를 모아놓은 모듈 입니다.
 ViewModel에서 Domain Layer의 Usecase로 가져온 데이터를 LiveData 또는 DataBinding를 통해 UI에 이벤트를 전달 합니다.

 * ####Domain Layer
 Usecase 패턴을 이용하여 비즈니스 로직을 구현한 모듈 입니다. Repository 인터페이스를 구현하여 Data Layer에서 처리 된 데이터를 mapper하여 Presenter Layer로 제공합니다.

 * ####Data Layer
 각각 네트워크, 데이터베이스에서 구현한 DataSource의 데이터값을 mapper하여  Repository 에서 가공하는 모듈입니다. Repository에 가공된 데이터는  domain Layer에 넘겨주는 역활을 합니다.
 
 >Presenter Layer , Data Layer는 오로지 Presenter Layer를 통해서 데이터를 주고 받습니다.
 
 <br></br>
 ## 언어 및 라이브러리, 툴
* 언어 : Kotlin
* 아키텍쳐 패턴 : AAC MVVM
* 비동기 : Coroutine
* DI : Koin 
* Android Jetpack
    * Foundation : Android KTX, Appcompat
    * Architecture : Data Binding , Lifecycles, Paging, LiveData, Navigation, Room, ViewModel
    * UI : Fragment, Layout
* ETC : Glide, Firestore, Firestorage , TedPermission, Timber
<br></br>
 ## 안드로이드 버전

    androidBuildToolsVersion = "29.0.2"
    androidMinSdkVersion = 23
    androidTargetSdkVersion = 29
    androidCompileSdkVersion = 29
