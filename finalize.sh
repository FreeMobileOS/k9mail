BASEDIR=$(dirname $0)
PRODUCT_PATH=$1
echo $PRODUCT_PATH

# zipalign
zipalign -v -p 4 $BASEDIR/k9mail/build/outputs/apk/k9mail-release-unsigned.apk $BASEDIR/k9mail/build/outputs/apk/k9mail-release-unsigned-aligned.apk

# sign apk with platform key
java -Xmx1024m -Djava.library.path="$BASEDIR/../../../out/host/linux-x86/lib64" -jar $BASEDIR/../../../out/host/linux-x86/framework/signapk.jar $BASEDIR/../../../build/target/product/security/platform.x509.pem $BASEDIR/../../../build/target/product/security/platform.pk8 $BASEDIR/k9mail/build/outputs/apk/k9mail-release-unsigned-aligned.apk $PRODUCT_PATH/system/app/k9mail.apk
