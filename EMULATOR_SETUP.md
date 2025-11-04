# Android Emulator Setup Guide

## Common Issue: DNS Not Configured

After resetting emulator data or creating a new emulator, DNS servers may not be configured, causing network errors like:
```
java.net.UnknownHostException: Unable to resolve host "api.o2.sk"
```

## Quick Fix

Run the provided helper script:
```bash
./fix-emulator-dns.sh
```

This script will:
1. Configure Google DNS (8.8.8.8 and 8.8.4.4)
2. Disable airplane mode
3. Install the app
4. Verify the configuration

## Manual Fix

If the script doesn't work, run these commands manually:

```bash
# Get root access
adb root

# Configure DNS
adb shell "setprop net.dns1 8.8.8.8"
adb shell "setprop net.dns2 8.8.4.4"

# Verify
adb shell "getprop net.dns1"
adb shell "getprop net.dns2"

# Reinstall app
./gradlew installDebug
```

## Permanent Solution

To avoid this issue every time, configure your AVD with DNS settings:

### Option 1: AVD Manager (Recommended)
1. Open Android Studio → Tools → AVD Manager
2. Click ▼ next to your emulator → Edit
3. Click "Show Advanced Settings"
4. Under "Network", set:
   - **DNS Server 1**: 8.8.8.8
   - **DNS Server 2**: 8.8.4.4
5. Click "Finish"

### Option 2: Launch with DNS Parameters
```bash
emulator -avd <your_avd_name> -dns-server 8.8.8.8,8.8.4.4
```

### Option 3: Edit config.ini
1. Find your AVD directory:
   ```bash
   ls ~/.android/avd/
   ```
2. Edit the AVD's `config.ini`:
   ```bash
   nano ~/.android/avd/<your_avd_name>.avd/config.ini
   ```
3. Add these lines:
   ```ini
   dns.server = 8.8.8.8
   dns.server2 = 8.8.4.4
   ```
4. Save and restart the emulator

## Testing API Connectivity

After configuring DNS, test the O2 API:

```bash
# Monitor API calls in logcat
adb logcat | grep -E "(O2Api|ScratchCard|Activation)"
```

Then in the app:
1. Launch the app
2. Click "Scratch Card"
3. After scratching, click "Activate"
4. Should successfully call https://api.o2.sk/version

Expected response format:
```json
{
  "android": "413506",
  "ios": "6.42",
  ...
}
```

## Troubleshooting

### DNS not persisting
- Use Option 1 (AVD Manager) for permanent configuration
- The script fix is temporary and resets on emulator restart

### Still getting UnknownHostException
```bash
# Check DNS is set
adb shell "getprop | grep dns"

# Check network connectivity
adb shell "ip route show"

# Restart network
adb shell "svc wifi disable && svc wifi enable"
```

### App works on WiFi but not on mobile data
- In emulator settings, ensure mobile data is enabled
- Or use WiFi in emulator (usually works better)
