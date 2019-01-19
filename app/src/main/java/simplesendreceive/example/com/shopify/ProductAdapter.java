package simplesendreceive.example.com.shopify;

import android.content.Context;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Product selectedProduct = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_item, parent, false);
        }

        selectedProduct.calculateInventory();

        //set the values of the relevant text views to selected product's values
        TextView inventoryTextView = convertView.findViewById(R.id.inventoryAmount);
        String inventoryQuantity = Integer.toString(selectedProduct.getInventoryQuantity());
        inventoryTextView.setText("Total Available: " + inventoryQuantity);

        TextView productNameTextView = convertView.findViewById(R.id.productTitle);
        productNameTextView.setText(selectedProduct.getTitle());

        DownloadImageBackgroundTask newImagePull = new DownloadImageBackgroundTask();
        Bitmap bit;

        try {
            bit = (Bitmap) newImagePull.execute(selectedProduct.getImage().getSrc()).get();

            ImageView collectionImageView = convertView.findViewById(R.id.productImage);
            collectionImageView.setImageBitmap(bit);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return convertView;
    }



}