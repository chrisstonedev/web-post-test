using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace CJStone.WebPostTest.Desktop
{
    public partial class Form1 : Form
    {
        private static readonly HttpClient client = new HttpClient();
        private const string TAG = "MainActivity";
        private const string ID = "id";
        private const string DATE = "date";
        private const string SOURCE = "source";
        private const string TEXT = "text";

        public Form1()
        {
            InitializeComponent();
            RefreshDisplay();
        }

        private async Task PostTextAsync(String text)
        {
            try
            {
                await SendPostAsync("posttext", "text", text);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
            RefreshDisplay();
        }

        private async System.Threading.Tasks.Task PostDeleteAsync(String id)
        {
            try
            {
                await SendPostAsync("postdelete", "id", id);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
            RefreshDisplay();
        }

        private string GetJsonUrl(String url)
        {
            string response = string.Empty;
            try
            {
                WebClient client = new WebClient();
                response = client.DownloadString(url);
                //response = await client.GetStringAsync(url);
                Console.WriteLine(response);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
            return response;
        }

        private void RefreshDisplay()
        {
            String url = "http://cjstone.net/webposttest/gettext.php";
            try
            {
                string response = GetJsonUrl(url);
                JArray data = JArray.Parse(response);
                List<ListViewItem> items = new List<ListViewItem>();
                foreach (ListViewItem lvi in listView.Items)
                    items.Add(lvi);
                foreach (JObject jsonObject in data)
                {
                    string id = jsonObject[ID].ToString();
                    string date = DateTime.Parse(jsonObject[DATE].ToString()).AddHours(3).ToString("G");
                    string source = jsonObject[SOURCE].ToString();
                    string text = jsonObject[TEXT].ToString();

                    bool createCard = true;
                    for (int i = 0; i < items.Count; i++)
                    {
                        if (items[i].Tag.ToString() == id)
                        {
                            items.RemoveAt(i);
                            createCard = false;
                            break;
                        }
                    }
                    if (!createCard)
                        continue;

                    ListViewItem listViewItem = new ListViewItem(new string[] { date, source, text })
                    {
                        Tag = id
                    };

                    listView.Items.Add(listViewItem);
                }
                foreach (ListViewItem lvi in items)
                    listView.Items.Remove(lvi);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        private async Task SendPostAsync(String urlPage, String label, String value)
        {
            String url = "http://cjstone.net/webposttest/" + urlPage + ".php";

            var values = new Dictionary<string, string>
            {
                { "source", "Windows" },
                { label, value }
            };

            var content = new FormUrlEncodedContent(values);

            var response = await client.PostAsync(url, content);
            Console.WriteLine("\r\nSending 'POST' request to URL : " + url);
            Console.WriteLine("Response Code : " + response.StatusCode.ToString());
        }

        private void RefreshButton_Click(object sender, EventArgs e)
        {
            RefreshDisplay();
        }

        private async void SendButton_Click(object sender, EventArgs e)
        {
            await SendButtonAsync();
        }

        private async Task SendButtonAsync()
        {
            await PostTextAsync(textBox.Text);
            textBox.Text = string.Empty;
        }

        private async void ListView_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Delete && listView.SelectedItems.Count == 1)
                if (MessageBox.Show("Delete message?", "Web Post Test", MessageBoxButtons.YesNo) == DialogResult.Yes)
                    await DeleteButtonAsync();
        }

        private async Task DeleteButtonAsync()
        {
            await PostDeleteAsync(listView.SelectedItems[0].Tag.ToString());
            textBox.Text = string.Empty;
        }
    }
}
