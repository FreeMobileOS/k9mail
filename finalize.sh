BASEDIR=$(dirname $0)
PRODUCT_PATH=$1
echo Product path:$PRODUCT_PATH
echo Android path:$ANDROID_SRC_PATH
# zipalign
zipalign -v -p 4 $BASEDIR/k9mail/build/outputs/apk/k9mail-release-unsigned.apk $BASEDIR/k9mail/build/outputs/apk/k9mail-release-unsigned-aligned.apk

if [ -z "$ANDROID_SRC_PATH" ] ; then
    echo "Android source path is not set. Sign apk manually"
    return 0
fi

# sign apk with platform key
java -Xmx1024m -Djava.library.path="$ANDROID_SRC_PATH/out/host/linux-x86/lib64" -jar $ANDROID_SRC_PATH/out/host/linux-x86/framework/signapk.jar $ANDROID_SRC_PATH/build/target/product/security/platform.x509.pem $ANDROID_SRC_PATH/build/target/product/security/platform.pk8 $BASEDIR/k9mail/build/outputs/apk/k9mail-release-unsigned-aligned.apk $PRODUCT_PATH/k9mail.apk
