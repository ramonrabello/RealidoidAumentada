package br.com.androidnarede.realidoid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openintents.intents.WikitudeARIntent;
import org.openintents.intents.WikitudePOI;

import wikitude.teste.R;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Aplicação apresentada durante o
 * AndroidConf Brasil 2011, que demonstra
 * como integrar a Wikitude API em Android
 * para criar uma aplicações com realidade
 * aumentada.
 * 
 * @author ramonrabello
 * @since 26 de Novembro de 2011
 */
public class RealidoidActivity extends Activity {
    
	private WikitudeARIntent wikitudeIntent;
	private List<WikitudePOI> pontosDeInteresse;
	
	// infos de localização
	private Location location;
	private LocationManager locationManager;
	private Double latitude;
	private Double longitude;
	private Double altitude;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        pontosDeInteresse = new ArrayList<WikitudePOI>();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
       
        // se o GPS estiver ativo, captura a localizacao do aparelho,
        // caso contrário, simula uma localização
        if (location != null){
        	   latitude = location.getLatitude();
        	   longitude = location.getLongitude();
        	   altitude = location.getAltitude();
        } else {
        	   // simulando as coordenadas
        	   latitude = -1.274309;
        	   longitude = -48.501892;
        	   altitude = location.getAltitude();
        }
        
		wikitudeIntent = new WikitudeARIntent(getApplication(), 
				                              getResources().getString(R.string.application_key), 
				                              getResources().getString(R.string.developer_name));
		wikitudeIntent.addTitleText("Android na Rede");
		
        Geocoder geocoder = new Geocoder(this);
		
		try {
			
			// recupera as ocorrências dos endereços de acordo com a latitude e longitude do aparelho
			List<Address> enderecos = geocoder.getFromLocation(latitude, 
					                                           longitude, 10);
			
			for (Address endereco : enderecos) {
				WikitudePOI wikitudePoi = new WikitudePOI( latitude, 
                                                           longitude, 
                                                           altitude, 
                                                           endereco.getFeatureName(),
                                                           endereco.getFeatureName() + ", " +
                                                           endereco.getCountryName() + " - " + endereco.getPostalCode() + "(" + endereco.getPhone() + ")");
				pontosDeInteresse.add(wikitudePoi);
			}
			wikitudeIntent.addPOIs(pontosDeInteresse);
		} catch (IOException e) {
			Toast.makeText(this, "Erro ao obter localização!", Toast.LENGTH_LONG).show();
		}
		wikitudeIntent.startIntent(this);
    }
}