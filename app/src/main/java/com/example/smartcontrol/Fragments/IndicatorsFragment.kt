package com.example.smartcontrol.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.smartcontrol.ControlViewModel
import com.example.smartcontrol.R
import com.example.smartcontrol.SensorViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [IndicatorsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IndicatorsFragment : Fragment() {
    private lateinit var view: View
    private val sensorViewModel: SensorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Indicators", "Indicators Fragment created")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        view = inflater.inflate(R.layout.fragment_indicators, container, false)

        val tempView = view.findViewById<TextView>(R.id.air_temperature_value)
        val humView = view.findViewById<TextView>(R.id.air_humidity_value)
        val waterView = view.findViewById<TextView>(R.id.water_level_value)
        val groundView = view.findViewById<TextView>(R.id.ground_humidity_value)
        val pumpIndicator = view.findViewById<TextView>(R.id.pump_indicator)
        val modeIndicator = view.findViewById<TextView>(R.id.mode_selected_value)
        val irrigationTimeIndicator = view.findViewById<TextView>(R.id.pump_time)
        val irrigationTimeSelected = view.findViewById<TextView>(R.id.irrigation_time_selected)

        sensorViewModel.globalStatus.observe(viewLifecycleOwner, Observer { globalStatus ->
            tempView.text =  getString(R.string.degrees, globalStatus.temperature)
            humView.text = globalStatus.airHumidity + " %"
            waterView.text = globalStatus.waterLevel + " %"
            groundView.text = globalStatus.groundHumidity + " %"
            modeIndicator.text = if (globalStatus.auto) getString(R.string.auto)
            else getString(R.string.manual)

            pumpIndicator.text = if (globalStatus.pumpStarted) getString(R.string.pump_activated)
            else getString(R.string.pump_deactivated)

            val timeSelected = globalStatus.autoHourScheduled.toString() + ":" +
                    globalStatus.autoMinuteScheduled.toString()
            if (globalStatus.auto)  irrigationTimeSelected.text = timeSelected
            else irrigationTimeSelected.text = ""

            irrigationTimeIndicator.alpha = if (globalStatus.auto)  1f else 0.5f
            irrigationTimeIndicator.text = convertirMilis(globalStatus.irrigationTime)
        })

        sensorViewModel.getGlobalStatus("1", 5_000)

        return view
    }

    private fun convertirMilis(ms: Long): String {
        val totalSeconds = ms / 1000
        val totalMinutes = totalSeconds / 60
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        val seconds = totalSeconds % 60

        return String.format("%02d:%02d", minutes, seconds)
    }


}