package com.workseasy.com.repository

import com.hr.demoapp.network.RemoteDataSource
import com.jaredrummler.html.I
import com.workseasy.com.ui.purchase.response.CreateQuotationDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Query

class PurchaseRepository {
    private val client = RemoteDataSource.getInterface()
    suspend fun raisePr(
        item: RequestBody, make: RequestBody, gender: RequestBody,
        delivery: RequestBody, quantity: RequestBody, unit: RequestBody,
        remark: RequestBody, photo: MultipartBody.Part?,
    ) = client!!.raisePr(item, make, gender, delivery, quantity, unit, remark, photo)

    suspend fun getItemList() = client!!.getItemsList()

    suspend fun getPrAssetsList(itemId: Int) = client!!.getPrAssetsList(itemId)

    suspend fun getSpinnerType(type: String) = client!!.getAssets(type)

    suspend fun getPrList() = client!!.getPrList()

    suspend fun getApprovedPrList(status: String?) = client!!.getApprovedPrList(status)


    suspend fun viewPr(pr_id: Int?) = client!!.viewPr(pr_id)

    suspend fun updatePr(pr_id: Int?, status: String?, remark: String?) = client!!.updatePrStatus(
        pr_id, status, remark
    )


    suspend fun grnStore(
        po: RequestBody,
        item: RequestBody,
        remark: RequestBody,
        bill_no: RequestBody,
        receipt_date: RequestBody,
        quantity: RequestBody,
        photo: MultipartBody.Part?,
    ) = client!!.grnStore(po, item, remark, bill_no, receipt_date, quantity, photo)

    suspend fun consumptionStore(
        item: String,
        quantity: String,
        date: String,
        remark: String,
    ) = client!!.consumptionStore(item, quantity, date, remark)


    suspend fun getQuotationList() = client!!.getQuotationList();

    suspend fun getPrListForQuotation(
        prid: Int?,
    ) = client!!.getPrListForQuotation(prid);


    suspend fun updateQuotation(
        quotation_id: Int?,
        status: String
    ) = client!!.quotationUpdate(quotation_id, status)

    suspend fun createQuotation(
        createQuotationDto: CreateQuotationDto,
    ) = client!!.createQuotation(createQuotationDto)

    suspend fun getGrnItemData(
    ) = client!!.getGrnItemData()

    suspend fun getGrnPoData(poId: Int) = client!!.getGrnPoData(poId)

    suspend fun getConsumptionItemData() = client!!.getConsumptionData()

    suspend fun getQuotationForApproval() = client!!.getQuotationForApproval()




}