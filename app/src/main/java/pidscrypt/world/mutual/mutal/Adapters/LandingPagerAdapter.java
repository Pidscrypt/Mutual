package pidscrypt.world.mutual.mutal.Adapters;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pidscrypt.world.mutual.mutal.ChatsFragment;
import pidscrypt.world.mutual.mutal.ContactsFragment;
import pidscrypt.world.mutual.mutal.InstaFragment;
import pidscrypt.world.mutual.mutal.MutualsFragment;

public class LandingPagerAdapter extends FragmentStatePagerAdapter {

    Drawable instaIcon;

    public LandingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                return new ContactsFragment();
            case 1:
                return new ChatsFragment();
            case 2:
                return new MutualsFragment();
            case 3:
                return new InstaFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch(position) {
            case 0:
                return "";
            case 1:
                return "Chats";
            case 2:
                return "Mutuals";
            case 3:
                return "";
            default:
                return null;
        }
    }

}
