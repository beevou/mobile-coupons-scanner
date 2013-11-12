mobile-coupons-scanner
======================

Android app to scan mobile coupons using beevou.com framework.


GETTING STARTED. -----------------

First you have to insert your clientID and clientSecret in src/libraries/BeevouFunctions.java
You can get your clientID and clientSecret in My Account Section via beevou.net, you have to sign up first.

After that you will be able to connect and interact with coupons and vouchers

It is recomended to change the password for the storage of the user credentials in the device, to do that change the value of
the variable seed in the file /src/com/beevou/android/scanner/Beevou_Scanner.java 

static String seed = "MY SEED Password";

FOR MORE INFO IN mobile coupons go to
http://www.beevou.co.uk

To get into the beevou api docs go to
http://www.beevou.co.uk/pages/mobile_coupons_developers_info.html

If you need futher help do not hesitate to contact beevou support 
http://support.beevou.net/
