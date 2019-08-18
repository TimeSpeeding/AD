using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Group13SSIS.Models
{
    public class DisbursementVM
    {
        public Dictionary<int?, List<Disbursement>> Disbursements { get; set; }
        public Dictionary<int?, Dept> Depts { get; set; }

        public class Dept
        {
            public int? DeptId { get; set; }
            public string DeptName { get; set; }
            public string RepName { get; set; }
            public string Point { get; set; }
        }
        public class Disbursement
        {
            public int DisbursementDetailId { get; set; }
            public int? DeptId { get; set; }
            public string StationeryDescription { get; set; }
            public int? NeededQty { get; set; }
            public int? RetrievalQty { get; set; }
            public int? DeliveryQty { get; set; }
            public string Comment { get; set; }
        }
    }
}