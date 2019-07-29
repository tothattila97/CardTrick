package attila.toth.production.cardtrick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import attila.toth.production.cardtrick.adapter.MagicItemAdapter;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "PICVALUE";

    private RecyclerView recyclerView;
    private MagicItemAdapter adapter;

    private String picvalue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();

        /*TextView descriptiontv = (TextView) findViewById(R.id.sensor_usage_description);
        Spinner resourcespinner = (Spinner) findViewById(R.id.resource_chooser);
        Button startserviceb = (Button) findViewById(R.id.start_service);

        startserviceb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(),"A választott kép: " + picvalue, Toast.LENGTH_SHORT).show();
                Intent intentStartService = new Intent(MainActivity.this, CardShowingService.class);
                intentStartService.putExtra(TAG, picvalue);
                startService(intentStartService);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_resources, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        resourcespinner.setAdapter(adapter);
        resourcespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: //R.drawable.blue_card_back:
                        picvalue = getString(R.string.blue);
                        break;
                    case 1://R.drawable.red_card_back:
                        picvalue = getString(R.string.red);
                        break;
                    case 2://R.drawable.green_card_back:
                        picvalue = getString(R.string.green);
                        break;
                    case 3://R.drawable.quater_dollar_coin:
                        picvalue = getString(R.string.quadollar);
                        break;
                    case 4:
                        picvalue = getString(R.string.ketszazft);
                        break;
                    default:

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.help:
                //TODO: Feldobni a súgót
                //Toast.makeText(getBaseContext(),"Lépek a súgóra", Toast.LENGTH_SHORT).show();
                Intent helpIntent = new Intent(this, HelpActivity.class);
                startActivity(helpIntent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.magicitemRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        adapter = new MagicItemAdapter(new MagicItemAdapter.OnMagicItemSelectedListener() {
            @Override
            public void onMagicItemSelected(String item) {
                /*Intent intentStartService = new Intent(MainActivity.this, CardShowingService.class);
                picvalue = item;
                intentStartService.putExtra(TAG, picvalue);
                startService(intentStartService);*/
            }
        });
        adapter.addMagicItem(getString(R.string.recycleblue));
        adapter.addMagicItem(getString(R.string.recyclered));
        adapter.addMagicItem(getString(R.string.recyclegreen));
        adapter.addMagicItem(getString(R.string.recyclequater));
        adapter.addMagicItem(getString(R.string.recycleketszaz));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
