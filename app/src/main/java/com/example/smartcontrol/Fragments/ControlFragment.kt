package com.example.smartcontrol.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.Observer
import com.example.smartcontrol.ControlPost
import com.example.smartcontrol.ControlViewModel
import com.example.smartcontrol.PumpMode
import com.example.smartcontrol.R



/**
 * A simple [Fragment] subclass.
 * Use the [ControlFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ControlFragment : Fragment() {
    private lateinit var view: View
    private val controlViewModel: ControlViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Control", "Control Fragment created")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        view = inflater.inflate(R.layout.fragment_control, container, false)

        val modeManual = view.findViewById<Button>(R.id.manual)
        val modeAuto = view.findViewById<Button>(R.id.auto)
        val startButton = view.findViewById<Button>(R.id.start_button)
        val stopButton = view.findViewById<Button>(R.id.stop_button)
        val activationTimeSelector = view.findViewById<TimePicker>(R.id.activation_time)
        val periodicModeSelector = view.findViewById<SwitchCompat>(R.id.periodic_mode_switch)
        val repeatDaysSelector = view.findViewById<SeekBar>(R.id.repeat_days)
        val repeatDaysIndicator = view.findViewById<TextView>(R.id.repeatDaysIndicator)

        val appContext = requireContext().applicationContext
        //val deveui = getNetConfig(appContext)?.getProperty("deveui")

        // initialize seek bar repeat days
        repeatDaysIndicator.text = 1.toString()
        val defaultDays = repeatDaysIndicator.text.toString().toInt()
        repeatDaysSelector.progress = defaultDays
        activationTimeSelector.setIs24HourView(true)

        controlViewModel.getControlMode("1", 5_000)
        controlViewModel.controlMode.observe(viewLifecycleOwner, Observer { controlMode ->
            if (controlMode.auto) {
                startButton.isEnabled = false
                stopButton.isEnabled = false
            } else {
                startButton.isEnabled = true
                stopButton.isEnabled = true
            }
        })

        modeManual.setOnClickListener{
            Log.d("Control", "Send manual control")
            Toast.makeText(appContext,"Send manual control",Toast.LENGTH_SHORT).show()

            val newControlPost = ControlPost("manual")
            controlViewModel.setControl("1", newControlPost)

        }

        modeAuto.setOnClickListener{
            Log.d("Control", "Send auto control")
            Toast.makeText(appContext,"Send auto control",Toast.LENGTH_SHORT).show()
            val activationHour = activationTimeSelector.hour
            val activationMinute = activationTimeSelector.minute
            val periodicMode = periodicModeSelector.isChecked
            val repeatDays = repeatDaysSelector.progress
            val newControlPost = ControlPost("auto", activationHour, activationMinute,
                periodicMode, repeatDays)
            controlViewModel.setControl("1", newControlPost)
        }


        startButton.setOnClickListener {
            Log.d("Control", "Start Pump")
            Toast.makeText(appContext,"Send Start Pump",Toast.LENGTH_SHORT).show()
            val newPumpMode = PumpMode(true)
            controlViewModel.startPump("1",newPumpMode)
        }

        stopButton.setOnClickListener {
            Log.d("Control", "Stop Pump")
            Toast.makeText(appContext,"Send Stop Pump",Toast.LENGTH_SHORT).show()
            val newPumpMode = PumpMode(false)
            controlViewModel.startPump("1",newPumpMode)
        }

        repeatDaysSelector.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, currentValue: Int, p2: Boolean) {
                repeatDaysIndicator.text = currentValue.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        return view
    }
}