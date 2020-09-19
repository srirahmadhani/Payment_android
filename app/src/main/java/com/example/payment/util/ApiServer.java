package com.example.payment.util;

public class ApiServer {
    public static final String site_url = "http://192.168.100.47/payment/public/api/";
    public static final String set_regist = site_url+"registrasi";
    public static final String get_tiket = site_url+"ticket";
    public static final String get_login = site_url+"login";
    public static final String get_profil = site_url+"visitor/";
    public static final String edit_profil = site_url+"edit-profil/";
    public static final String payment = site_url+"payment";
    public static final String get_qr = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=";
    public static final String get_img = "http://192.168.100.47/payment/public/image/";
}
