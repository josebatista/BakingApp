package com.example.jpereira.bakingapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jpereira.bakingapp.R;
import com.example.jpereira.bakingapp.databinding.RecipeStepsItemListBinding;
import com.example.jpereira.bakingapp.domain.Step;

import java.util.ArrayList;

/**
 * Created by jpereira on 14/08/17.
 */

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepViewHolder> {

    private ArrayList<Object> mSteps;
    private Context mContext;
    private StepListClickListener mClickHandle;

    public interface StepListClickListener {
        void onClick(int position);
    }

    public StepListAdapter(StepListClickListener mClickHandle) {
        this.mClickHandle = mClickHandle;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recipe_steps_item_list, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        holder.mBinding.stepsTvQtd.setText(String.valueOf(position) + ". ");
        Object step = mSteps.get(position);
        if (step instanceof String) {
            holder.mBinding.stepsTvStepDescription.setText(step.toString());
            holder.mBinding.stepsIv.setVisibility(View.INVISIBLE);
        } else if (step instanceof Step) {
            holder.mBinding.stepsTvStepDescription.setText(((Step) step).getShortDescription());
            if (!((Step) step).getVideoURL().isEmpty()) {
                holder.mBinding.stepsIv.setImageResource(R.drawable.ic_live_tv);
            } else if (!((Step) step).getThumbnailURL().isEmpty()) {
                holder.mBinding.stepsIv.setImageResource(R.drawable.ic_image);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mSteps != null) {
            return mSteps.size();
        } else {
            return 0;
        }
    }

    public void swapSteps(ArrayList<Object> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RecipeStepsItemListBinding mBinding;

        public StepViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandle.onClick(position);
        }
    }
}
