package com.dllopis.gettingsafe.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dllopis.gettingsafe.R;

import java.util.ArrayList;

public class TripAdapter extends ArrayAdapter<Trayecto> {

    private ArrayList lastsTrips;
    private Context mContext;

    public TripAdapter(Context context, ArrayList<Trayecto> lastsTrips) {
        super(context, R.layout.trip, lastsTrips);
        this.mContext = context;
        this.lastsTrips = lastsTrips;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater vi = LayoutInflater.from(mContext);
        View item = vi.inflate(R.layout.trip, null);

        TextView origenText = item.findViewById(R.id.origin);
        origenText.setText(((Trayecto)lastsTrips.get(position)).getOrigen());

        TextView destinoText = item.findViewById(R.id.destiny);
        destinoText.setText(((Trayecto)lastsTrips.get(position)).getDestino());

        TextView methodText = item.findViewById(R.id.method);
        methodText.setText(((Trayecto)lastsTrips.get(position)).getMetodo());

        TextView tiempoText = item.findViewById(R.id.time);
        tiempoText.setText(String.valueOf(((Trayecto)lastsTrips.get(position)).getTiempo()));

        return item;
    }

}
