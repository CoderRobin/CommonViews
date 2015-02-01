package com.coderrobin.customview;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

/*
 * code by CoderRobin
 */
public class NestRadioGroup extends LinearLayout{
	public interface OnCheckedChangeListener{

        void onCheckedChanged(NestRadioGroup group, int checkedId);
	}
	private OnCheckedChangeListener mOnCheckedChangeListener;
	private ArrayList<CompoundButton> mCompoundButtonList=new ArrayList<CompoundButton>();
	private int mCurrentCheckedId=-1;
	
	
	
	public NestRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NestRadioGroup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		initCompoundButtonList();
		this.setOnHierarchyChangeListener(new OnHierarchyChangeListener(){

			@Override
			public void onChildViewAdded(View parent, View child) {
				findCompoundButtons(child);
			}

			@Override
			public void onChildViewRemoved(View parent, View child) {
				removeCompoundButtons(child);
				
			}
			
		});
	}
	
	


	public void initCompoundButtonList(){
		findCompoundButtons(this);
		for(CompoundButton compoundButton:mCompoundButtonList){
		compoundButton.setOnCheckedChangeListener(mChildOnCheckedChangeListener);
		}
	}
	
	private void findCompoundButtons(View view){
		if(view instanceof CompoundButton){
			mCompoundButtonList.add((CompoundButton)view);
		}
		else if(view instanceof ViewGroup){
			ViewGroup viewGroup=(ViewGroup)view;
			int childSize=viewGroup.getChildCount();
			for(int i=0;i<childSize;i++){
				findCompoundButtons(viewGroup.getChildAt(i));
			}
		}
	}
	
	private void removeCompoundButtons(View view){
		if(view instanceof CompoundButton){
			mCompoundButtonList.remove((CompoundButton)view);
		}
		else if(view instanceof ViewGroup){
			ViewGroup viewGroup=(ViewGroup)view;
			int childSize=viewGroup.getChildCount();
			for(int i=0;i<childSize;i++){
				removeCompoundButtons(viewGroup.getChildAt(i));
			}
		}
	}
	
	
	
	public void setChecked(int checkedId){
		for(CompoundButton compoundButton:mCompoundButtonList){
			if(compoundButton.getId()==checkedId){
				compoundButton.setChecked(true);
			}
			else {
				compoundButton.setChecked(false);
			}
		}
	}
	
	public void setOnCheckedChangeListener(OnCheckedChangeListener pOnCheckedChangeListener){
		mOnCheckedChangeListener=pOnCheckedChangeListener;
	}
	
	public int getCheckedId(){
		return mCurrentCheckedId;
	}
	
	private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener=new CompoundButton.OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton pCompoundButton, boolean checked) {
			if(checked){
				for(CompoundButton compoundButton:mCompoundButtonList){
					if(compoundButton!=pCompoundButton){
						if(compoundButton.isChecked()){
							compoundButton.setChecked(false);
						}
					}
				}
				mCurrentCheckedId=pCompoundButton.getId();
				if(mOnCheckedChangeListener!=null){
					mOnCheckedChangeListener.onCheckedChanged(NestRadioGroup.this, pCompoundButton.getId());
				}
			}
		}
		
	};
	
	

}
