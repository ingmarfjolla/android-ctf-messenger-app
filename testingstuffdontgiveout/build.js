
// partially sourced from : https://github.com/rsenet/FriList/blob/main/02_SecurityBypass/DebugMode_Emulator/android-emulator-detection-bypass.js#L32
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



if (Module.findExportByName("libc.so", "open") !== null) {
    Interceptor.attach(Module.findExportByName("libc.so", "open"), {
        onEnter: function (args) {
            var path = Memory.readCString(args[0]);
            console.log("open() called with: " + path);
            // Optionally, check if the file path is one you want to block:
            if(path.indexOf("/proc/self/task/") !== -1){ this.block = true; }
            //this.block = true; // Block all open calls, for demonstration.
        },
        onLeave: function (retval) {
            if (this.block) {
                console.log("Blocking file access. Returning error (-1).");
                retval.replace(-1);
            }
        }
    });
}