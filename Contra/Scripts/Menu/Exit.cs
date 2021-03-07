using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class Exit : MonoBehaviour
{
    public string menu = "Menu";

    public void MainMenu()
    {
        GameManager.isAlive = true;
        SceneManager.LoadScene(menu);
        Time.timeScale = 1;
        Cursor.lockState = CursorLockMode.None;
    }
}
