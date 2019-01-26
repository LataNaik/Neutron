package enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum Platform {
	IOS, IOS_WEB, ANDROID, ANDROID_WEB, DESKTOP_WEB;

	private static Set<Platform> desktopWebPlatforms = new HashSet<Platform>(
			Arrays.asList(new Platform[] { Platform.DESKTOP_WEB }));
	private static Set<Platform> mobileNativePlatforms = new HashSet<Platform>(
			Arrays.asList(new Platform[] { Platform.IOS, Platform.ANDROID }));
	private static Set<Platform> mobileWebPlatforms = new HashSet<Platform>(
			Arrays.asList(new Platform[] { Platform.IOS_WEB, Platform.ANDROID_WEB }));

	private static Set<Platform> iosPlatforms = new HashSet<Platform>(
			Arrays.asList(new Platform[] { Platform.IOS, Platform.IOS_WEB }));

	private static Map<Platform, IdentificationPlatform> platformIdMap = new HashMap<Platform, IdentificationPlatform>() {
		{
			put(Platform.IOS, IdentificationPlatform.IOS);
			put(Platform.IOS_WEB, IdentificationPlatform.MWEB);
			put(Platform.ANDROID, IdentificationPlatform.ANDROID);
			put(Platform.ANDROID_WEB, IdentificationPlatform.MWEB);
			put(Platform.DESKTOP_WEB, IdentificationPlatform.DESKTOP_WEB);
		}
	};

	public static boolean isMobilePlatform(Platform platform) {
		return !desktopWebPlatforms.contains(platform);
	}

	public static boolean isMobileNativePlatform(Platform platform) {
		return mobileNativePlatforms.contains(platform);
	}

	public static boolean isMobileWebPlatform(Platform platform) {
		return mobileWebPlatforms.contains(platform);
	}

	public static boolean isWebPlatform(Platform platform) {
		return mobileWebPlatforms.contains(platform) || desktopWebPlatforms.contains(platform);
	}

	public static boolean isIosPlatform(Platform platform) {
		return iosPlatforms.contains(platform);
	}

	public static IdentificationPlatform asIdentificationPlatform(Platform platform) {
		return platformIdMap.get(platform);
	}

	public static IdentificationPlatform asIdentificationPlatform(String name) {
		return platformIdMap.get(Platform.asPlatform(name));
	}

	public static Platform asPlatform(String name) {
		return Platform.valueOf(name.toUpperCase());
	}
}
