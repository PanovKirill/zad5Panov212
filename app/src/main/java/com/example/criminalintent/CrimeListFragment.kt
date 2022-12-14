package com.example.criminalintent

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Update
import java.util.UUID
import java.util.zip.Inflater
import kotlin.math.log

private const val TAG = "CrimeListFragment";

class CrimeListFragment : Fragment() {
    private lateinit var crimeRecyclerView: RecyclerView
    private  var adapter : CrimeAdapter ?= CrimeAdapter(emptyList())

    interface CallBacks{
        fun onCrimeSelected(crimeId: UUID)
    }

    private var callbacks: CallBacks?= null

    private val crimeListViewModel:
            CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks =context as CallBacks?
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)


        crimeRecyclerView.adapter = adapter
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListViewData.observe(viewLifecycleOwner,observer {
                crimes -> crimes.let{
                    Log.i(TAG, "Got crimes ${crimes.size}")
                    updataUI(crimes)
                }
        })
    }

    override fun onDetach() {
        super.onDetach()
        callbacks=null
    }



    private  fun UpdateUI(crimes: List<Crime>){

    }
    private inner class CrimeHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val titleTextView: TextView =
            itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView =
            itemView.findViewById(R.id.crime_date)
        private lateinit var crime: Crime

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime:Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
        }

        override fun onClick(v: View) {
            callbacks?.onCrimeSelected(crime.id)
        }
    }

    private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)
        }


        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }
        override fun getItemCount() = crimes.size
    }
    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}

