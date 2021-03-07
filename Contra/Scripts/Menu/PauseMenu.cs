using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class PauseMenu : MonoBehaviour
{
    public static bool GameIsPaused = false;
    public GameObject pauseMenuUI;
    public GameObject mainPausedMenuUI;
    public GameObject optionsMenuUI;
    public GameObject GameOverMenuUI;
    public GameObject crosshair;
    public GameObject player;

    public string menu = "Menu";
    public string lvl = "SampleScene";

    // Update is called once per frame
    void Update()
    {
        if (Input.GetKeyDown(KeyCode.Escape))
        {
            if (GameIsPaused)
            {
                Resume();
            }
            else
            {
                Pause();
            }
        }

        if (!GameManager.isAlive)
        {
            GameOver();
        }
    }
    public void Resume()
    {
        pauseMenuUI.SetActive(false);
        Time.timeScale = 1;
        GameIsPaused = false;
        Cursor.lockState = CursorLockMode.Locked;
        crosshair.GetComponent<Image>().enabled = true;
    }

    void Pause()
    {
        pauseMenuUI.SetActive(true);
        Time.timeScale = 0;
        GameIsPaused = true;
        Cursor.lockState = CursorLockMode.None;
        crosshair.GetComponent<Image>().enabled = false;
    }

    public void LoadMenu()
    {
        GameManager.isAlive = true;
        SceneManager.LoadScene(menu);
        Time.timeScale = 1;
    }

    public void QuitGame()
    {
        Debug.Log("Quit");
        Application.Quit();
    }

    void GameOver()
    {
        if (!player.GetComponent<Animator>().enabled)
            player.GetComponent<Animator>().enabled = true;
        GameOverMenuUI.SetActive(true);
        Time.timeScale = 0;
        Cursor.lockState = CursorLockMode.None;
        crosshair.GetComponent<Image>().enabled = false;
    }

    public void RestartGame()
    {
        GameManager.isAlive = true;
        SceneManager.LoadScene(lvl);
        Time.timeScale = 1;
    }
}
