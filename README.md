# PasswordInputView
一个用来输入密码的自定义控件

从砍掉的需求中扒出来的一个控件，时间紧暂时没有抽出取属性和方法，先看图吧

![](https://github.com/ileelay/PasswordInputView/blob/master/screen/demo.gif)

使用起来比较简单

```
PasswordInputView passwordInputView = (PasswordInputView) findViewById(R.id.passwordInputView);
        passwordInputView.setMaxLength(4);
        passwordInputView.setOnInputCallback(new PasswordInputView.OnInputCallback() {
            @Override
            public void onInputComplete(String inputText) {
                Toast.makeText(MainActivity.this, inputText, Toast.LENGTH_SHORT).show();
            }
        });

```
  
现实原理很简单：

1. 主要是定义一个透明的EditText,名字为HindEditText,主要用来弹起键盘和接受事件

2. 一个能Draw图片的TextView命名为ImageTextView，用来显示图片或者文字

3. 一个ItemView，继承自ImagTextView,用来控制文字图片显示和计时切换
 
4. 一个PasswordInputView布局，继承自FrameLayout，用来装HindEditText和一个LinearLayout，当然这个LinearLayout存放的是N个ItemView

5. PasswordInputView监听HindEditText事件，控制ItemView的显示，对外提供回调
