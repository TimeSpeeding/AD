using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Group13SSIS.Models
{
    public class ReoederVM
    {
        public int ReorderId { get; set; }
        public string SupplierId { get; set; }
        public string ClerkId { get; set; }
        public double Amount { get; set; }
        public System.DateTime Date { get; set; }
        public string Status { get; set; }
    }
}