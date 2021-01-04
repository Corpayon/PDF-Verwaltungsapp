package com.example.meinuniverwalter;

import android.content.Context;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.view.ViewGroup;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import java.io.File;
import java.io.IOException;
import androidx.viewpager.widget.PagerAdapter;
import de.number42.subsampling_pdf_decoder.PDFDecoder;
import de.number42.subsampling_pdf_decoder.PDFRegionDecoder;

public class PDFPagerAdapter extends PagerAdapter {

    private Context context;

    private File file;

    private ParcelFileDescriptor mFileDescriptor;

    private float scale;


    private PdfRenderer renderer;


    public PDFPagerAdapter(Context context, File file) {
        super();
        this.context = context;
        this.file = file;
        this.scale = 8;
        try {
            mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            renderer = new PdfRenderer(mFileDescriptor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public Object instantiateItem(ViewGroup container, int position) {



        SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(context);

        int minimumTileDpi = 120;
        imageView.setMinimumTileDpi(minimumTileDpi);

        imageView.setBitmapDecoderFactory(() -> new PDFDecoder(position, file, scale));


        imageView.setRegionDecoderFactory(() -> new PDFRegionDecoder(position, file, scale));

        ImageSource source = ImageSource.uri(file.getAbsolutePath());

        imageView.setImage(source);

        container.addView(imageView);
        return imageView;
    }


    public int getCount() {
        return renderer.getPageCount();
    }

    @Override public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

