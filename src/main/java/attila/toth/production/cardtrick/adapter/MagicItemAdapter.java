package attila.toth.production.cardtrick.adapter;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import attila.toth.production.cardtrick.MainActivity;
import attila.toth.production.cardtrick.R;
import attila.toth.production.cardtrick.service.CardShowingService;


public class MagicItemAdapter extends RecyclerView.Adapter<MagicItemAdapter.MagicItemViewHolder> {

    public List<String> magicitems;
    public final OnMagicItemSelectedListener listener;

    public MagicItemAdapter(OnMagicItemSelectedListener listener) {
        this.listener = listener;
        magicitems = new ArrayList<>();
    }

    @Override
    public MagicItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_magic, parent, false);
        MagicItemViewHolder viewHolder = new MagicItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MagicItemViewHolder holder, int position) {
        holder.position = position;
        holder.nameTextView.setText(magicitems.get(position));
        holder.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentStartService = new Intent(v.getContext(), CardShowingService.class);
                String picvalue = holder.nameTextView.getText().toString();
                intentStartService.putExtra(MainActivity.TAG, picvalue);
                v.getContext().startService(intentStartService);
            }
        });
    }

    @Override
    public int getItemCount() {
        return magicitems.size();
    }

    public void addMagicItem(String newCity) {
        magicitems.add(newCity);
        notifyItemInserted(magicitems.size() - 1);
    }

    public class MagicItemViewHolder extends RecyclerView.ViewHolder {

        int position;

        TextView nameTextView;
        Button startButton;

        public MagicItemViewHolder(View itemView) {
            super(itemView);
            nameTextView =
                    (TextView) itemView.findViewById(
                            R.id.magic_itemTv);
            startButton = (Button) itemView.findViewById(
                    R.id.startButton);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onMagicItemSelected(magicitems.get(position));
                    }
                }
            });
        }
    }

    public interface OnMagicItemSelectedListener {
        void onMagicItemSelected(String city);
    }
}