package fragments;

import Interfaces.OnDrawerToggleListner;
import Interfaces.OnItemRemoveListener;
import Interfaces.OnQuantityChangeListener;
import adapters.CartEditListAdapter;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import cart.ItemCart;
import com.example.maaz.olo.R;
import com.example.maaz.olo.screens.MainActivity;

/**
 * Created by Maaz on 6/9/2016.
 */
public class OrderCheckoutFragment extends Fragment{

    TextView editOrderTv, applyOrderChanges, addProduct, orderSubTotal, orderAllTotal;
    int subTotal = 0;
    int allTotal = 0;
    Button btn_checkout;
    private OnDrawerToggleListner mListner;

    public OrderCheckoutFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_order_checkout_screen, container, false);
        initViews(rootView);

        OrderCheckSimpleListFragment simpleListFragment = new OrderCheckSimpleListFragment();
        android.app.FragmentManager fragment = getFragmentManager();
        android.app.FragmentTransaction transaction = fragment.beginTransaction();
        transaction.replace(R.id.orderframe_container, simpleListFragment);
        transaction.commit();
        return rootView ;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListner.showDrawerToggle(true);
        hideKeyboard();

    }
    private void hideKeyboard(){

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Order Checkout");



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListner= (OnDrawerToggleListner) context;

        }
        catch (ClassCastException ex){
            throw new ClassCastException("must implement OnDrawerToggleListner");
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        try {

            mListner= (OnDrawerToggleListner) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnDrawerToggleListner");
        }
    }

    private void initViews(View view) {

        editOrderTv = (TextView) view.findViewById(R.id.tv_editOrder);
        editOrderTv.setOnClickListener(new EditableOrderItemListner());

        applyOrderChanges = (TextView)view.findViewById(R.id.tv_editApplyOrder);
        applyOrderChanges.setOnClickListener(new EditApplyOrderListner());

        addProduct = (TextView)view.findViewById(R.id.addProductBtn);
        addProduct.setOnClickListener(new AddProductListner());

        orderSubTotal = (TextView)view.findViewById(R.id.tv_subTotal);
        subTotal = (int) ItemCart.getInstance().getTotal();
        orderSubTotal.setText(Integer.toString(subTotal));

        orderAllTotal = (TextView)view.findViewById(R.id.tv_allTotal);
        allTotal = (int) ItemCart.getInstance().getAllTotal();
        orderAllTotal.setText(Integer.toString(allTotal));
        btn_checkout= (Button) view.findViewById(R.id.checkoutBtn);
        checkOrderItem();
        btn_checkout.setOnClickListener(new InfoCheckListner());
    }

    private void checkOrderItem(){

       // int size=ItemCart.getOrderableItems().size();
        if(ItemCart.getOrderableItems().isEmpty()){
//            hide order checkout button
            btn_checkout.setVisibility(View.INVISIBLE);
        }
        else {

            btn_checkout.setVisibility(View.VISIBLE);
        }
    }


    private class EditableOrderItemListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            editOrderTv.setVisibility(v.GONE);
            applyOrderChanges.setVisibility(v.VISIBLE);

            OrderCheckEditListFragment editListFragment = new OrderCheckEditListFragment();
            android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);

           // transaction.setCustomAnimations(R.animator.slide_out_left, R.animator.slide_in_right,R.animator.slide_in_left, R.animator.slide_out_right);
            transaction.setCustomAnimations( R.animator.card_flip_right_in, R.animator.card_flip_right_out, R.animator.card_flip_left_in, R.animator.card_flip_left_out);
            OrderCheckSimpleListFragment simpleListFragment = new OrderCheckSimpleListFragment();
            transaction.hide(simpleListFragment);
            transaction.replace(R.id.orderframe_container, editListFragment);
            transaction.commit();
        }
    }

    private class EditApplyOrderListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            applyOrderChanges.setVisibility(v.GONE);

            OrderCheckSimpleListFragment simpleListFragment = new OrderCheckSimpleListFragment();
            android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
            // transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_left, R.animator.slide_in_right, R.animator.slide_out_right);
            // transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
            // transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_out_left, R.animator.slide_in_right);
            // transaction.setCustomAnimations(R.animator.slide_out_left, R.animator.slide_in_right,R.animator.slide_in_left, R.animator.slide_out_right);
            reloadFragment();
            updatePriceListner();
            OrderCheckEditListFragment editListFragment = new OrderCheckEditListFragment();
            transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
            transaction.hide(editListFragment);
            transaction.replace(R.id.orderframe_container, simpleListFragment);
            transaction.commit();

        }
    }

    private void updatePriceListner() {
        MainActivity.onQuantityChangeListener.onQuantityChanged((int) ItemCart.getInstance().getTotal());
    }

    private void reloadFragment() {

        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }

    private class AddProductListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
        }
    }

    private class InfoCheckListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            UserInfo infofragment = new UserInfo();
            android.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, infofragment).commit();

        }
    }
}
