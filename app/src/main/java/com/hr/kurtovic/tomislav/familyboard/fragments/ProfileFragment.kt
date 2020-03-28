package com.hr.kurtovic.tomislav.familyboard.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.hr.kurtovic.tomislav.familyboard.MainActivity
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private var currentUser: FamilyMember? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?)
            : View? = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        profile_image.setOnClickListener { changeProfile() }
//        getCurrentUserFromFirestore()

        logout.setOnClickListener { logOut() }
    }

    private fun logOut() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseAuth.getInstance().signOut()
            (activity as? MainActivity)?.showLoginScreen()
        }
    }

    //todo implement changing of profile image

    //Have to add RxPermission for local storage and file picker
    private fun changeProfile() {
        Snackbar.make(view!!, "Profile image change", Snackbar.LENGTH_LONG).show()
    }

//    private fun getCurrentUserFromFirestore() {
//        UserService.getUser(FirebaseAuth.getInstance().currentUser!!.uid)
//                .addOnSuccessListener { documentSnapshot ->
//                    currentUser = documentSnapshot.toObject(User::class.java)
//                    loadProfileImage()
//                    setUserInfo()
//                }
//    }

    private fun loadProfileImage() {
        if (currentUser?.urlPicture != null) {
            Glide.with(this)
                    .load(currentUser?.urlPicture)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profile_image)
        }
    }

    private fun setUserInfo() {
        user_name.text = currentUser?.name

        if (currentUser?.role != null) {
            user_role.setText(currentUser?.role)
        }
    }

}
