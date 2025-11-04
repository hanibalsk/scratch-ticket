#!/bin/bash
# Complete Android Emulator Network Fix
# This script fixes networking issues in Android emulators

echo "🔧 Fixing Android Emulator Network Configuration..."
echo ""

# Step 1: Root access
echo "1️⃣  Getting root access..."
adb root
sleep 2

# Step 2: Add default gateway
echo "2️⃣  Adding default gateway..."
adb shell "ip route add default via 10.0.2.2 dev eth0" 2>/dev/null || echo "   (Gateway already exists)"

# Step 3: Configure DNS
echo "3️⃣  Configuring DNS servers..."
adb shell "setprop net.dns1 8.8.8.8"
adb shell "setprop net.dns2 8.8.4.4"

# Step 4: Configure DNS for interfaces
echo "4️⃣  Configuring interface DNS..."
adb shell "setprop net.eth0.dns1 8.8.8.8"
adb shell "setprop net.eth0.dns2 8.8.4.4"
adb shell "setprop net.wlan0.dns1 8.8.8.8"
adb shell "setprop net.wlan0.dns2 8.8.4.4"

# Step 5: Restart network
echo "5️⃣  Restarting network services..."
adb shell "svc wifi disable"
sleep 2
adb shell "svc wifi enable"
sleep 3

# Step 6: Test connectivity
echo ""
echo "🧪 Testing connectivity..."
echo -n "   Ping 8.8.8.8: "
if adb shell "ping -c 1 -W 2 8.8.8.8" >/dev/null 2>&1; then
    echo "✅ Success"
else
    echo "❌ Failed"
fi

echo -n "   DNS lookup: "
if adb shell "ping -c 1 -W 2 google.com" >/dev/null 2>&1; then
    echo "✅ Success"
else
    echo "⚠️  DNS not working - Emulator restart required"
fi

echo ""
echo "📋 Current Network Configuration:"
echo "   Gateway: $(adb shell 'ip route show default')"
echo "   DNS 1:   $(adb shell 'getprop net.dns1')"
echo "   DNS 2:   $(adb shell 'getprop net.dns2')"

echo ""
echo "📱 Installing app..."
./gradlew installDebug

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "⚠️  If internet still doesn't work in Chrome:"
echo ""
echo "   CLOSE THE EMULATOR and restart it with:"
echo "   emulator -avd <your_avd_name> -dns-server 8.8.8.8,8.8.4.4"
echo ""
echo "   Or set permanently in AVD Manager:"
echo "   Android Studio → Tools → AVD Manager → Edit → Advanced"
echo "   Set DNS Server 1: 8.8.8.8, DNS Server 2: 8.8.4.4"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
