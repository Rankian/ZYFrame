package com.sanjie.zy.picture.ui.fragment.mvp;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.Toast;

import com.sanjie.zy.R;
import com.sanjie.zy.picture.bean.ImageFolder;
import com.sanjie.zy.picture.bean.ImageItem;
import com.sanjie.zy.widget.ZYToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LangSanJie on 2017/4/25.
 */

public class ZYPickerFragmentPresenter extends ZYPickerFragmentContract.Presenter {
    /**
     * Media attribute.
     */
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media._ID, // image id.
            MediaStore.Images.Media.DATA, // image absolute path.
            MediaStore.Images.Media.DISPLAY_NAME, // image name.
            MediaStore.Images.Media.DATE_ADDED, // The time to be added to the library.
            MediaStore.Images.Media.BUCKET_ID, // folder id.
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME // folder name.
    };

    @Override public void start() {

    }

    /**
     * Scan the list of pictures in the library.
     */
    private Observable<List<ImageFolder>> getPhotoAlbum(final Context context) {

        return Observable.create(new ObservableOnSubscribe<List<ImageFolder>>() {
            @Override public void subscribe(ObservableEmitter<List<ImageFolder>> e) throws Exception {

                Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES);
                Map<String, ImageFolder> albumFolderMap = new HashMap<>();

                ImageFolder allImageImageFolder = new ImageFolder();
                allImageImageFolder.setChecked(true);
                allImageImageFolder.setName(context.getString(R.string.all_phone_album));

                while (cursor.moveToNext()) {
                    int imageId = cursor.getInt(0);
                    String imagePath = cursor.getString(1);
                    String imageName = cursor.getString(2);
                    long addTime = cursor.getLong(3);

                    int bucketId = cursor.getInt(4);
                    String bucketName = cursor.getString(5);

                    ImageItem ImageItem = new ImageItem(imageId, imagePath, imageName, addTime);
                    allImageImageFolder.addPhoto(ImageItem);

                    ImageFolder imageFolder = albumFolderMap.get(bucketName);
                    if (imageFolder != null) {
                        imageFolder.addPhoto(ImageItem);
                    } else {
                        imageFolder = new ImageFolder(bucketId, bucketName);
                        imageFolder.addPhoto(ImageItem);

                        albumFolderMap.put(bucketName, imageFolder);
                    }
                }
                cursor.close();
                List<ImageFolder> imageFolders = new ArrayList<>();

                Collections.sort(allImageImageFolder.getImages());
                imageFolders.add(allImageImageFolder);

                for (Map.Entry<String, ImageFolder> folderEntry : albumFolderMap.entrySet()) {
                    ImageFolder imageFolder = folderEntry.getValue();
                    Collections.sort(imageFolder.getImages());
                    imageFolders.add(imageFolder);
                }
                e.onNext(imageFolders);
                e.onComplete();
            }
        });
    }

    @Override public void loadAllImage(final Context context) {
        getPhotoAlbum(context).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override public void accept(@NonNull Disposable disposable) throws Exception {
                        view.showWaitDialog();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override public void run() throws Exception {
                        view.hideWaitDialog();
                    }
                })
                .subscribe(new Consumer<List<ImageFolder>>() {
                    @Override public void accept(@NonNull List<ImageFolder> imageFolders) throws Exception {
                        view.showAllImage(imageFolders);
                    }
                });
    }
}
