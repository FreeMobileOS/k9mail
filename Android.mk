LOCAL_PATH := $(call my-dir)

# Note: Make sure ANDROID_HOME is exported with sdk path
$(info $(shell ($(LOCAL_PATH)/gradlew assembleRelease -p $(LOCAL_PATH)/)) )
$(info $(shell ($(LOCAL_PATH)/finalize.sh $(PRODUCT_OUT_PATH))))


