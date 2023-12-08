package com.workseasy.com.ui.hradmin.employeeRegistration.response

import android.os.Parcel
import android.os.Parcelable

data class DataX (
    val aadharNumber: String?,
    val age: String?,
    val dateOfBirth: String?,
    val department: String?,
    val designation: String?,
    val email: String?,
    val fatherName: String?,
    val gender: String?,
    val husband: String?,
    val maritialStatus: String?,
    val mobNo: String?,
    val name: String?,
    val panNumber: String?,
    val qualification: String?,
    val selReligion: String?,
    val dateOfJoining: String?,
    val payCode: String?,
    val photo : String?,
    val aadhar_front_photo: String?,
    val aadhar_back_photo: String?,
    val pan_front_photo : String?
): Parcelable   {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!


    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(this.aadharNumber)
        dest?.writeString(this.age)
        dest?.writeString(this.dateOfBirth)
        dest?.writeString(this.department)
        dest?.writeString(this.designation)
        dest?.writeString(this.email)
        dest?.writeString(this.fatherName)
        dest?.writeString(this.gender)
        dest?.writeString(this.husband)
        dest?.writeString(this.maritialStatus)
        dest?.writeString(this.mobNo)
        dest?.writeString(this.name)
        dest?.writeString(this.panNumber)
        dest?.writeString(this.qualification)
        dest?.writeString(this.selReligion)
        dest?.writeString(this.dateOfJoining)
        dest?.writeString(this.payCode)
        dest?.writeString(this.photo)
        dest?.writeString(this.aadhar_front_photo)
        dest?.writeString(this.aadhar_back_photo)
        dest?.writeString(this.pan_front_photo)

    }

    companion object CREATOR : Parcelable.Creator<DataX> {
        override fun createFromParcel(parcel: Parcel): DataX {
            return DataX(parcel)
        }

        override fun newArray(size: Int): Array<DataX?> {
            return arrayOfNulls(size)
        }
    }

}