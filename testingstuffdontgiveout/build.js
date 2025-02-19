
// source : https://github.com/rsenet/FriList/blob/main/02_SecurityBypass/DebugMode_Emulator/android-emulator-detection-bypass.js#L32
Java.perform(function()
{
    console.log("--> Anti Emulator Detection Bypass (aka BluePill) - Script Loaded")

    bypass_build_properties()

});


function replaceFinaleField(object, fieldName, value)
{
    var field =  object.class.getDeclaredField(fieldName)
    field.setAccessible(true)
    field.set(null, value)
}

function bypass_build_properties()
{
        console.log("Build Properties - Bypass Loaded")
        // Class containing const that we want to modify
        const Build = Java.use("android.os.Build")

        // reflection class for changing const
        const Field = Java.use('java.lang.reflect.Field')
        const Class = Java.use('java.lang.Class')

        // Replacing Build static fields
        replaceFinaleField(Build, "FINGERPRINT", "abcd/C1505:4.1.1/11.3.A.2.13:user/release-keys")
        replaceFinaleField(Build, "MODEL", "C1505")
        replaceFinaleField(Build, "MANUFACTURER", "Sony")
        replaceFinaleField(Build, "BRAND", "Xperia")
        replaceFinaleField(Build, "BOARD", "7x27")
        replaceFinaleField(Build, "ID", "11.3.A.2.13")
        replaceFinaleField(Build, "SERIAL", "abcdef123")
        replaceFinaleField(Build, "TAGS", "release-keys")
        replaceFinaleField(Build, "USER", "administrator")
}
