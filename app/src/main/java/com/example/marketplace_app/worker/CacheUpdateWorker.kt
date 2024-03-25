import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.marketplace_app.BroadcastReceiver
import com.example.marketplace_app.R
import com.example.marketplace_app.data.repository.CartRepository
import com.example.marketplace_app.utils.notification.ProductNotification
import com.example.marketplace_app.utils.notification.ProductNotificationManager
import com.example.marketplace_app.utils.repository.DataRepository
import javax.inject.Inject

class DataCheckWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    private val repository: DataRepository,
    private val cartRepository: CartRepository,
) : CoroutineWorker(context, params) {


    override suspend fun doWork(): Result {
        val cachedData = repository.retrieveDataFromCache()
        val newData = cartRepository.getAllProductsFromCart()

        if (cachedData != newData) {
            sendNotification()
        }

        return Result.success()
    }

    private fun sendNotification() {
        val intent = Intent(applicationContext, BroadcastReceiver::class.java).apply {
            action = "TEST_ACTION"
        }
        applicationContext.sendBroadcast(intent)
    }
}
