package com.mycheering.vpf.install;



public class InstallerManager {
	
	private static IInstaller mInstaller;

    public InstallerManager() {
    }

    public static void addInstaller(IInstaller installer) {
        mInstaller = installer;
    }

    public static IInstaller getInstaller() {
        if(mInstaller == null) {
            mInstaller = new IInstaller() {
                public void installPackage(String fileName, IInstallerCallback callback) {
                    callback.finishInstall(-1);
                }
            };
        }

        return mInstaller;
    }

}
