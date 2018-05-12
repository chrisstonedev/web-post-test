namespace CJStone.WebPostTest.Desktop
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.Windows.Forms.Panel panel;
            System.Windows.Forms.ColumnHeader dateColumnHeader;
            System.Windows.Forms.ColumnHeader sourceColumnHeader;
            System.Windows.Forms.ColumnHeader textColumnHeader;
            this.panelListView = new System.Windows.Forms.Panel();
            this.listView = new System.Windows.Forms.ListView();
            this.panelBottom = new System.Windows.Forms.Panel();
            this.textBox = new System.Windows.Forms.TextBox();
            this.panelButtons = new System.Windows.Forms.Panel();
            this.sendButton = new System.Windows.Forms.Button();
            this.refreshButton = new System.Windows.Forms.Button();
            panel = new System.Windows.Forms.Panel();
            dateColumnHeader = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            sourceColumnHeader = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            textColumnHeader = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            panel.SuspendLayout();
            this.panelListView.SuspendLayout();
            this.panelBottom.SuspendLayout();
            this.panelButtons.SuspendLayout();
            this.SuspendLayout();
            // 
            // panel
            // 
            panel.BackColor = System.Drawing.SystemColors.GradientActiveCaption;
            panel.Controls.Add(this.panelListView);
            panel.Controls.Add(this.panelBottom);
            panel.Dock = System.Windows.Forms.DockStyle.Fill;
            panel.Location = new System.Drawing.Point(0, 0);
            panel.Name = "panel";
            panel.Padding = new System.Windows.Forms.Padding(5);
            panel.Size = new System.Drawing.Size(412, 298);
            panel.TabIndex = 0;
            // 
            // panelListView
            // 
            this.panelListView.Controls.Add(this.listView);
            this.panelListView.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panelListView.Location = new System.Drawing.Point(5, 5);
            this.panelListView.Name = "panelListView";
            this.panelListView.Padding = new System.Windows.Forms.Padding(5);
            this.panelListView.Size = new System.Drawing.Size(402, 210);
            this.panelListView.TabIndex = 2;
            // 
            // listView
            // 
            this.listView.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            dateColumnHeader,
            sourceColumnHeader,
            textColumnHeader});
            this.listView.Dock = System.Windows.Forms.DockStyle.Fill;
            this.listView.FullRowSelect = true;
            this.listView.GridLines = true;
            this.listView.Location = new System.Drawing.Point(5, 5);
            this.listView.MultiSelect = false;
            this.listView.Name = "listView";
            this.listView.Size = new System.Drawing.Size(392, 200);
            this.listView.TabIndex = 0;
            this.listView.UseCompatibleStateImageBehavior = false;
            this.listView.View = System.Windows.Forms.View.Details;
            this.listView.KeyDown += new System.Windows.Forms.KeyEventHandler(this.ListView_KeyDown);
            // 
            // dateColumnHeader
            // 
            dateColumnHeader.Text = "Date";
            dateColumnHeader.Width = 100;
            // 
            // sourceColumnHeader
            // 
            sourceColumnHeader.Text = "Source";
            // 
            // textColumnHeader
            // 
            textColumnHeader.Text = "Text";
            textColumnHeader.Width = 240;
            // 
            // panelBottom
            // 
            this.panelBottom.Controls.Add(this.textBox);
            this.panelBottom.Controls.Add(this.panelButtons);
            this.panelBottom.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.panelBottom.Location = new System.Drawing.Point(5, 215);
            this.panelBottom.Name = "panelBottom";
            this.panelBottom.Padding = new System.Windows.Forms.Padding(5);
            this.panelBottom.Size = new System.Drawing.Size(402, 78);
            this.panelBottom.TabIndex = 1;
            // 
            // textBox
            // 
            this.textBox.Dock = System.Windows.Forms.DockStyle.Fill;
            this.textBox.Location = new System.Drawing.Point(5, 5);
            this.textBox.Multiline = true;
            this.textBox.Name = "textBox";
            this.textBox.Size = new System.Drawing.Size(313, 68);
            this.textBox.TabIndex = 0;
            // 
            // panelButtons
            // 
            this.panelButtons.Controls.Add(this.sendButton);
            this.panelButtons.Controls.Add(this.refreshButton);
            this.panelButtons.Dock = System.Windows.Forms.DockStyle.Right;
            this.panelButtons.Location = new System.Drawing.Point(318, 5);
            this.panelButtons.Name = "panelButtons";
            this.panelButtons.Size = new System.Drawing.Size(79, 68);
            this.panelButtons.TabIndex = 3;
            // 
            // sendButton
            // 
            this.sendButton.Location = new System.Drawing.Point(9, 6);
            this.sendButton.Name = "sendButton";
            this.sendButton.Size = new System.Drawing.Size(60, 23);
            this.sendButton.TabIndex = 1;
            this.sendButton.Text = "Send";
            this.sendButton.UseVisualStyleBackColor = true;
            this.sendButton.Click += new System.EventHandler(this.SendButton_Click);
            // 
            // refreshButton
            // 
            this.refreshButton.Location = new System.Drawing.Point(9, 35);
            this.refreshButton.Name = "refreshButton";
            this.refreshButton.Size = new System.Drawing.Size(60, 23);
            this.refreshButton.TabIndex = 2;
            this.refreshButton.Text = "Refresh";
            this.refreshButton.UseVisualStyleBackColor = true;
            this.refreshButton.Click += new System.EventHandler(this.RefreshButton_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(412, 298);
            this.Controls.Add(panel);
            this.Name = "Form1";
            this.Text = "Web Post Test";
            panel.ResumeLayout(false);
            this.panelListView.ResumeLayout(false);
            this.panelBottom.ResumeLayout(false);
            this.panelBottom.PerformLayout();
            this.panelButtons.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Panel panelBottom;
        private System.Windows.Forms.ListView listView;
        private System.Windows.Forms.Button refreshButton;
        private System.Windows.Forms.Button sendButton;
        private System.Windows.Forms.TextBox textBox;
        private System.Windows.Forms.Panel panelButtons;
        private System.Windows.Forms.Panel panelListView;
    }
}

