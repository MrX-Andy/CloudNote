package com.stone.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ericssonlabs.R;
import com.google.zxing.WriterException;
import com.zxing.activity.CaptureActivity;
import com.zxing.encoding.EncodingHandler;

public class BarCodeTestActivity extends Activity {
    
	/** Called when the activity is first created. */
	private TextView resultTextView;
	private EditText qrStrEditText;
	private ImageView qrImgImageView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        resultTextView = (TextView) this.findViewById(R.id.tv_scan_result);
        qrStrEditText = (EditText) this.findViewById(R.id.et_qr_string);
        qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);
        
        /*Button Listening Event:  �������ɨ���ά��*/
        Button scanBarCodeButton = (Button) this.findViewById(R.id.btn_scan_barcode);
        scanBarCodeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent openCameraIntent = new Intent(BarCodeTestActivity.this,CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
			}
		});
        
        /**Button Listening Event:  �������֣����ɶ�ά�� **/
        Button generateQRCodeButton = (Button) this.findViewById(R.id.btn_add_qrcode);
        generateQRCodeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					String contentString = qrStrEditText.getText().toString();
					if (!contentString.equals("")) {
						final Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 350);
						qrImgImageView.setImageBitmap(qrCodeBitmap);
						
						/*ImageView Listening Event:  �򿪲˵��Ի���*/
						qrImgImageView.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								LayoutInflater factoryInflater = LayoutInflater.from(getApplicationContext());
								final View dialogView = factoryInflater.inflate(R.layout.dialog, null);
								Builder savephotoDialog = new AlertDialog.Builder(BarCodeTestActivity.this)
								.setTitle("��ʾ�� ")
								.setView(dialogView)
								.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										/* ����ͼƬ��������� */
										String path = "/sdcard/DCIM/Camera";
										int count = 2;
										boolean result = ToolKit.savePhotoToSDCard(path, "img_2013_11_11_Code" + (count++) +".jpg", qrCodeBitmap);
										if(result == true){
											Toast.makeText(BarCodeTestActivity.this, "����ɹ�", Toast.LENGTH_SHORT).show();
										}else{
											Toast.makeText(BarCodeTestActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
										}
									}
								})
								.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										
									}
								});
								savephotoDialog.show();  	/* ��ʾ�Ի��� */
							}
						});
					}else {
						Toast.makeText(BarCodeTestActivity.this, "Text can not be empty", Toast.LENGTH_SHORT).show();
					}
					
				} catch (WriterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			resultTextView.setText(scanResult);
		}
	}
	
}