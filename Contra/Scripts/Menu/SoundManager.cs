using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.Audio;

public class SoundManager : MonoBehaviour
{
    public AudioMixer masterMixer;
    public Slider slider;

    float soundLvl;

    public void SetSound(float soundLevel)
    {
        soundLvl = soundLevel;
        masterMixer.SetFloat("musicVol", soundLevel);
    }

    private void OnDisable()
    {
        PlayerPrefs.SetFloat("music", soundLvl);
    }

    private void OnEnable()
    {
        soundLvl = PlayerPrefs.GetFloat("music");
        masterMixer.SetFloat("musicVol", PlayerPrefs.GetFloat("music"));
        slider.value = soundLvl;
    }
}
