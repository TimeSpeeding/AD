using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Group13SSIS.Models
{
    public class RequisitionVM
    {
        public int RequisitionId { get; set; }
        public string ApplicantId { get; set; }
        public System.DateTime Date { get; set; }
        public string Status { get; set; }

        public string RejectReason { get; set; }
    }
}