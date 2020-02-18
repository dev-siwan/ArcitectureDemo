# Android Architecture Demo
## 프로젝트 소개

 #####  간단하게 Feed(게시물)을 CRUD하는 프로젝트 입니다.
 ##### Android Jecpack Component를 이용하였으며, 각각의 PresnterLayer, DataLayer, DomainLayer를 모듈로 나눠 UI와 Data를 독립적인 구조를 적용하였습니다.
<br></br>
 ## 프로젝트 모듈 도식화

![Alt text](https://github.com/DeveloperKimsiwan/ArcitectureDemo/blob/master/img/DemoArchitecture.png?raw=true)
 ##
 ## 언어 및 라이브러리, 툴


- ##### 언어 : Kotlin
- ##### 아키텍쳐 패턴 : AAC MVVM
- ##### 비동기 : Coroutine
- ##### DI : Koin 
- ##### Android Jetpack
    - ###### Foundation : Android KTX, Appcompat
    - ###### Architecture : Data Binding , Lifecycles, Paging, LiveData, Navigation, Room, ViewModel
    - ###### UI : Fragment, Layout
- ##### ETC : Glide, Firestore, Firestorage , TedPermission, Timber
 ## 안드로이드 버전

    androidBuildToolsVersion = "29.0.2"
    androidMinSdkVersion = 23
    androidTargetSdkVersion = 29
    androidCompileSdkVersion = 29

