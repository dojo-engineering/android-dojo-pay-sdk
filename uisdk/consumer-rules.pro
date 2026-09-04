# Consumer ProGuard/R8 rules shipped inside the AAR.
# These are applied automatically to any app that depends on this SDK,
# so integrators do not need to add anything to their own configuration.

# UI SDK entities are Serializable and travel between Activities via Intent
# extras. If R8 strips or renames them, Bundle.unparcel() fails and the payment
# flow Activities crash on launch.
-keep class tech.dojo.pay.uisdk.entities.** { *; }

# Raw API models consumed reflectively by Gson.
-keep class tech.dojo.pay.uisdk.data.entities.** { *; }

# Serializable contract required for java.io.Serialization to work. Scoped to the
# UI SDK package so R8 can still optimize Serializable classes in the host app.
-keepclassmembers class tech.dojo.pay.uisdk.** implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
