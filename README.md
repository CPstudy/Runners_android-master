# Runner's War

#### for android

***

## 최신 업데이트(2018-04-09)

##### 1. 레이아웃과 액티비티간의 연결

> LoginActivity: 로그인 액티비티
>
> JoinActivity: 회원가입 액티비티
>
> MainActivity: 메인 액티비티
>
> ModeSingleActivity: 싱글 플레이 액티비티
>
> ModeMultiActivity: 멀티 플레이 액티비티

##### 2. 애니메이션 추가

##### 3. 커스텀 Toast인 Sandwich 추가

> Sandwich의 사용법은 아래 문서 부분 참

##### 4. 자체 그래프 사용

> LineChart 클래스

***

## 문서

### Sandwich

안드로이드에서 제공하는 기본 토스트의 디자인이 앱 디자인과 상이한 부분이 많아 토스트의 기능을 이용해 제작한 메시지 창이다.

기존의 토스트와 사용 방법은 같다.

```java
Sandwich.makeText(context, message, duration).show();
```

##### `context: Context`

> getBaseContext() 사용을 권장한다.

##### `message: String`

> 메시지 내용을 적어준다.

##### `duration: int`

> `Sandwich.LENGTH_SHORT`, `Sandwich.LENGTH_LONG` 중 하나를 사용한다.

##### `show()`

> 메시지를 보여준다.

##### `show(gravity, xOffset, yOffset)`

> Sandwich의 위치를 지정할 수 있는 메소드로 Toast의 setGravity와 사용법이 같기 때문에 Toast의 안드로이드 문서를 알아서 찾아보도록.

***

### LineChart

##### 레이아웃 xml에서 사용하는 방법

```xml
<com.guk2zzada.runnerswar.LineChart
    android:id="@+id/chart"
    android:layout_width="match_parent"
    android:layout_height="200dp" />
```

##### 자바 코드에서 차트를 그리는 방법

findViewById()를 이용해 id값을 찾는 방법은 다음과 같다.

```java
LineChart chart = (LineChart) findViewById(R.id.chart);
```

차트에는 총 7개의 값을 넣어야 하며 int 배열이나 float 배열로 값을 넣을 수 있다.

LineChart 클래스에는 setData(int[])나 setData(float[])를 통해 값을 설정할 수 있다.

```java
int[] array = new int[]{8640, 6102, 6416, 7610, 9713, 8124, 8412, 9415};
chart.setData(array);
```

> 주의!
>
> 7개가 모두 들어가야 한다.

***

### GlobalVarious

안드로이드에서는 액티비티간 전역 변수를 사용할 수 있다. 여기서는 GlobalVarious 클래스를 이용해 전역변수를 사용한다.

> 아직까지는 아이디 값만 전역변수로 사용하고 있다.

```java
String id;
GlobalVarious gv = (GlobalVarious) getApplication();

gv.setId("runners");
id = gv.getId();
```

##### `setId(String)`

id값을 설정한다.

##### `String getId()`

id값을 반환한다.
