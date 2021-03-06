package xyz.u999.creations.numer.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.blunderer.materialdesignlibrary.views.CardView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import butterknife.ButterKnife;
import xyz.u999.creations.numer.BannerAd;
import xyz.u999.creations.numer.R;
import xyz.u999.creations.numer.RippleView;

/**
 * Created by umang on 27/6/16.
 */
public class MathsFactsFragment extends Fragment {

    CardView mathFactCard;
    RippleView fetchMathFact;
    TextInputEditText editTextMath;
    TextInputLayout getNumber;
    AdView bannerAdMaths;
    InputMethodManager inputMethodManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_math_facts, container, false);
        ButterKnife.bind(this, rootView);

        YoYo.with(Techniques.FadeInDown).duration(500).playOn(rootView);

        mathFactCard = (CardView) rootView.findViewById(R.id.math_card);
        getNumber = (TextInputLayout) rootView.findViewById(R.id.math_input);
        editTextMath = (TextInputEditText) rootView.findViewById(R.id.edit_math);
        fetchMathFact = (RippleView) rootView.findViewById(R.id.math_get_fact);
        bannerAdMaths = (AdView) rootView.findViewById(R.id.ad_banner_math);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        final AdRequest bannerAdDateRequest = BannerAd.getBannerAd();

        bannerAdMaths.postDelayed(new Runnable() {
            @Override
            public void run() {
                bannerAdMaths.loadAd(bannerAdDateRequest);
            }
        }, 300);

        editTextMath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mathFactCard.setVisibility(View.INVISIBLE);
            }
        });

        fetchMathFact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                String query = String.valueOf(getNumber.getEditText().getText());

                if (query.isEmpty())
                    Snackbar.make(rootView, "Please provide a number.", Snackbar.LENGTH_SHORT).show();
                else
                    new MathsFactsAsyncTask().execute(query);
            }
        });

        return rootView;
    }

    /**
     * Created by umang on 29/6/16.
     */
    class MathsFactsAsyncTask extends AsyncTask<String, Void, String> {

        String mathFactCardTitle = new String();

        @Override
        protected String doInBackground(String... params) {
            mathFactCardTitle = params[0];
            String url = "http://numbersapi.com/" + params[0] + "/math";
            String responseString = new String();
            try {
                Document doc = Jsoup.connect(url).get();
                Element body = doc.select("body").first();
                responseString = body.text();
                Log.wtf("wtf", responseString);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mathFactCard.setTitle("Math fact for number " + mathFactCardTitle);
            mathFactCard.setDescription(s);
            mathFactCard.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeInRight).duration(999).playOn(mathFactCard);
        }
    }
}
