package com.example.androidarchitecturepatterns.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPreferencesUtil {


    private var sp: SharedPreferences? = null

    /*fun read(context: Context): Boolean {
        val value: Boolean?
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        value = sp!!.getBoolean("login", false)
        return value
    }*/

    fun write(context: Context, login: Boolean) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putBoolean("login", login)
        editor.apply()
    }

    fun clear(context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.clear()
        editor.apply()

        write(context, false)
    }

    /*fun setUser(userDetails: UserDetailsBO, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("userDetails", userDetails.toString())
        editor.apply()
    }

    fun getUser(context: Context): UserDetails {
        sp = context.getSharedPreferences("MyPref", 0)
        return Gson().fromJson(sp!!.getString("userDetails", ""), UserDetails::class.java)
    }*/

    fun setToken(token: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun getToken(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("token", "")
    }

    fun setUserName(name: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("name", name)
        editor.apply()
    }

    fun getUserName(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("name", "")
    }

    fun setUserId(userId: String?, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("userId", userId)
        editor.apply()
    }

    fun getUserId(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("userId", "")
    }

    fun  setUserFirstName(userFName : String?, context: Context){
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("userFName", userFName)
        editor.apply()
    }

    fun getUserFirstName (context: Context): String?{
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("userFName", "")
    }

    fun setUserLastName (userLastName : String? , context: Context){
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("userLastName", userLastName)
        editor.apply()
    }

    fun getUserLastName (context: Context): String?{
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("userLastName", "")
    }

    fun setRememberMe(remember_me: String, context: Context){
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("remember_me", remember_me)
        editor.apply()
    }

    fun getRememberMe(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("remember_me", "")
    }

    fun setEmail(email: String?, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("email", email)
        editor.apply()
    }

    fun getEmail(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("email", "")
    }

    fun setPassword(password: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("password", password)
        editor.apply()
    }

    fun getPassword(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("password", "")
    }

    fun setImage(image: String?, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("image", image)
        editor.apply()
    }

    fun getImage(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("image", "")
    }


    fun setAddress(address: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("address", address)
        editor.apply()
    }

    fun getAddress(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("address", "")
    }

    fun setState(state: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("state", state)
        editor.apply()
    }

    fun getState(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("state", "")
    }

    fun setCity(city: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("city", city)
        editor.apply()
    }

    fun getCity(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("city", "")
    }

    fun setZip(zip: String, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("zip", zip)
        editor.apply()
    }

    fun getZip(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("zip", "")
    }

    fun setPhone(phone: String?, context: Context) {
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("phone", phone)
        editor.apply()
    }

    fun getPhone(context: Context): String? {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("phone", "")
    }

    fun setPhoneTwo (phoneTwo : String?,context: Context){
        sp = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sp!!.edit()
        editor.putString("phoneTwo", phoneTwo)
        editor.apply()
    }

    fun getPhoneTwo (context: Context) :String?
    {
        sp = context.getSharedPreferences("MyPref", 0)
        return sp!!.getString("phoneTwo", "")
    }
}