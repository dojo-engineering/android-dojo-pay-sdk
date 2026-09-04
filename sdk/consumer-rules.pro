# Consumer ProGuard/R8 rules shipped inside the AAR.
# These are applied automatically to any app that depends on this SDK,
# so integrators do not need to add anything to their own configuration.

# SDK entities are Serializable and travel between Activities via Intent extras.
# If R8 strips or renames them, Bundle.unparcel() fails, intent.extras becomes
# null and the payment Activities crash on launch.
-keep class tech.dojo.pay.sdk.card.entities.** { *; }
-keep class tech.dojo.pay.sdk.card.data.entities.** { *; }

# Returned to the host app through the ActivityResultContract as a Serializable.
-keep class tech.dojo.pay.sdk.DojoPaymentResult { *; }

# Generic Serializable contract required for java.io.Serialization to work.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Cardinal 3DS SDK relies on reflection internally.
-keep class com.cardinalcommerce.** { *; }
-dontwarn com.cardinalcommerce.**
