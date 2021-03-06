package net.yvesd.scfm;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class EditAccountsActivity extends ListActivity {

	private SharedPreferences params;
	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		GestionnaireThemes gt = new GestionnaireThemes(this);
		gt.chargerThemeChoisi();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setContentView(R.layout.editaccounts);
		lv = getListView();

		params = getSharedPreferences(SuiviConsoFreeMobileActivity.PREFS_NAME,
				0);

		final String[] comptes;
		String[] comptesNonFiltres;

		String comptesString = params.getString(
				SuiviConsoFreeMobileActivity.PREF_KEY_LISTE_COMPTES, "");
		if ("".equals(comptesString))
			comptesNonFiltres = new String[] {};
		else
			comptesNonFiltres = comptesString.split(",");

		comptes = filtrerComptesVides(comptesNonFiltres);

		String[] comptesAffichage = afficherPseudos(comptes);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, comptesAffichage));

		Button boutonCreerCompte = (Button) findViewById(R.id.createAccountButton);
		Button boutonRetour = (Button) findViewById(R.id.retour);

		boutonCreerCompte.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				creerOuMajCompte("");
			}
		});

		boutonRetour.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				retour();
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String login = comptes[position];

				creerOuMajCompte(login);
			}
		});
	}

	/**
	 * @deprecated Faire en sorte qu'il n'y ait pas de comptes vides à l'avenir
	 * 
	 * @return
	 */
	@Deprecated
	protected String[] filtrerComptesVides(String[] comptesNonFiltres) {

		List<String> listeFiltree = new ArrayList<String>();

		for (String compte : comptesNonFiltres) {
			if (!("".equals(compte)))
				listeFiltree.add(compte);
		}

		String[] resultat = new String[listeFiltree.size()];
		resultat = listeFiltree.toArray(resultat);
		return resultat;
	}

	/**
	 * Renvoie un tableau contenant les pseudos des comptes, s'ils sont définis
	 * dans les params. Si le pseudo d'un compte n'est pas renseigné, c'est son
	 * identifiant qui est retourné à la place
	 * 
	 * @param comptes
	 *            tableau d'identifiants de comptes
	 * @return tableau de comptes pour affichage graphique
	 */
	protected String[] afficherPseudos(String[] comptes) {
		String[] comptesAffichage = new String[comptes.length];

		for (int i = 0; i < comptes.length; i++) {
			String pseudo = params.getString(
					SuiviConsoFreeMobileActivity.PREF_KEYPREFIX_PSEUDO_ABO
							+ comptes[i], "");

			if ("".equals(pseudo))
				comptesAffichage[i] = comptes[i];
			else
				comptesAffichage[i] = pseudo;
		}
		return comptesAffichage;
	}

	private void creerOuMajCompte(String login) {
		Bundle bundle = new Bundle();

		bundle.putString(EditAccountActivity.LOGIN_ABO_CLE, login);

		Intent intent = new Intent(this, EditAccountActivity.class);
		intent.putExtras(bundle);

		startActivity(intent);
	}

	protected void retour() {
		finish();
	}
}
