using Group13SSIS.Models;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using PagedList;

namespace Group13SSIS.Controllers
{
    public class ClerkController : Controller
    {
        public ActionResult Index()
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var stationery = db.Stationeries.Where(x => x.Qty <= x.ReorderLevel).ToList();
                if (stationery != null) ViewBag.flag = true;
                else ViewBag.flag = false;
            }
            return View();
        }
        public ActionResult ReorderList()
        {
            List<ReoederVM> reorderlist = new List<ReoederVM>();
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var list = db.Reorders.ToList();
                foreach(var item in list)
                {
                    reorderlist.Add(new ReoederVM()
                    {
                        ReorderId = item.ReorderId,
                        SupplierId = db.Suppliers.Where(x => x.SupplierId == item.SupplierId).FirstOrDefault().Name,
                        ClerkId = db.Users.Where(x => x.UserId == item.ClerkId).FirstOrDefault().Name,
                        Amount = item.Amount,
                        Date = item.Date,
                        Status = item.Status
                    });
                }
            }
            return View(reorderlist);
        }
        public ActionResult ReorderDetail(int id)
        {
            List<ReorderDetailVM> detaillist = new List<ReorderDetailVM>();
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var a = db.Reorders.Where(x => x.ReorderId == id).FirstOrDefault();
                ViewData["reorder"] = new ReoederVM()
                {
                    ReorderId = a.ReorderId,
                    SupplierId = db.Suppliers.Where(x => x.SupplierId == a.SupplierId).FirstOrDefault().Name,
                    ClerkId = db.Users.Where(x => x.UserId == a.ClerkId).FirstOrDefault().Name,
                    Amount = a.Amount,
                    Date = a.Date,
                    Status = a.Status
                };
                var list = db.ReorderDetails.Where(x => x.ReorderId == id).ToList();
                foreach(var item in list)
                {
                    detaillist.Add(new ReorderDetailVM()
                    {
                        Stationery = db.Stationeries.Where(x => x.StationeryId == item.StationeryId).FirstOrDefault(),
                        Qty = item.Qty
                    });
                }
            }
            return View(detaillist);
        }
        public ActionResult ReceiveReorder(int id)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var reorder = db.Reorders.Where(x => x.ReorderId == id).FirstOrDefault();
                reorder.Status = "Received";
                var list = db.ReorderDetails.Where(x => x.ReorderId == id).ToList();
                foreach(var item in list)
                {
                    Stationery s = db.Stationeries.Where(x => x.StationeryId == item.StationeryId).FirstOrDefault();
                    s.Qty += item.Qty;
                }
                db.SaveChanges();
            }
            return RedirectToAction("ReorderList");
        }
        [HttpGet]
        public ActionResult CreateReorder()
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var stationery = db.Stationeries.Where(x => x.Qty <= x.ReorderLevel).ToList();
                ViewData["stationery"] = stationery;
            }
            return View();
        }
        [HttpPost]
        public ActionResult CreateReorder(int[] reorders)
        {
            User user = (User)Session["user"];
            Dictionary<int, List<ReorderDetail>> d = new Dictionary<int, List<ReorderDetail>>();
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                foreach (int i in reorders)
                {
                    var s = db.Stationeries.Where(x => x.StationeryId == i).FirstOrDefault();
                    var supplierId = db.SupplyDetails.Where(x => x.StationeryId == i).FirstOrDefault().FirstSupplierId;
                    int a = 0;
                    while (a + s.Qty < s.ReorderLevel) a += s.ReorderQty;
                    d.Add(supplierId, new List<ReorderDetail>());
                    d[supplierId].Add(new ReorderDetail()
                    {
                        StationeryId = i,
                        Qty = a
                    });
                }
                foreach (int key in d.Keys)
                {
                    double amount = 0;
                    foreach (var item in d[key])
                    {
                        var s = db.Stationeries.Where(x => x.StationeryId == item.StationeryId).FirstOrDefault();
                        amount += item.Qty * s.Price;
                    }
                    db.Reorders.Add(new Reorder()
                    {
                        SupplierId = key,
                        ClerkId = user.UserId,
                        Amount = amount,
                        Date = DateTime.Now,
                        Status = "Pending"
                    });
                    db.SaveChanges();
                    int reorderId = db.Reorders.Where(x => x.SupplierId == key && x.ClerkId == user.UserId && x.Amount == amount && x.Status == "Pending").FirstOrDefault().ReorderId;
                    foreach (var item in d[key])
                    {
                        item.ReorderId = reorderId;
                        db.ReorderDetails.Add(item);
                    }
                    db.SaveChanges();
                }
            }
            return RedirectToAction("ReorderList");
        }

        public ActionResult Stationery(string Category, string Search, int? page)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var cateLst = db.Stationeries.Select(x => x.Category).Distinct().ToList();
                ViewBag.Category = new SelectList(cateLst);
                var stationeries = db.Stationeries.ToList();
                if (!String.IsNullOrEmpty(Category))
                {
                    stationeries = stationeries.Where(x => x.Category == Category).ToList();
                }
                if (!String.IsNullOrEmpty(Search))
                {
                    stationeries = stationeries.Where(s => s.Description.ToLower().Contains(Search.ToLower())).ToList();
                }
                const int pageItems = 20;
                int currentPage = (page ?? 1);
                IPagedList<Stationery> pageStationeries = stationeries.ToPagedList(currentPage, pageItems);
                StationeryVM stationeryVM = new StationeryVM()
                {
                    Stationeries = pageStationeries,
                    Category = Category,
                    Search = Search
                };
                return View(stationeryVM);
            }
        }
        public ActionResult RequisitionList()
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var requisitionlist = new List<RequisitionVM>();
                var requisitions = db.Requisitions.Where(x => x.Status == "Approved").ToList();
                foreach (var r in requisitions)
                {
                    requisitionlist.Add(new RequisitionVM()
                    {
                        RequisitionId = r.RequisitionId,
                        ApplicantId = db.Users.Where(x => x.UserId == r.ApplicantId).FirstOrDefault().Name,
                        Date = r.Date
                    });
                }
                ViewData["requisitionlist"] = requisitionlist;
            }
            return View();
        }

        public ActionResult RequisitionDetails(int id)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var re = db.Requisitions.Where(x => x.RequisitionId == id).FirstOrDefault();
                RequisitionVM requisition = new RequisitionVM()
                {
                    RequisitionId = re.RequisitionId,
                    ApplicantId = db.Users.Where(x => x.UserId == re.ApplicantId).FirstOrDefault().Name,
                    Date = re.Date,
                    Status = re.Status,
                    RejectReason = re.RejectReason
                };
                ViewData["requisition"] = requisition;
                var details = db.RequisitionDetails.Where(x => x.RequisitionId == id).ToList();
                var list = new List<CartLine>();
                foreach (var item in details)
                {
                    CartLine cartLine = new CartLine()
                    {
                        Stationery = db.Stationeries.Where(x => x.StationeryId == item.StationeryId).FirstOrDefault(),
                        Qty = item.Quantity
                    };
                    list.Add(cartLine);
                }
                ViewData["list"] = list;
            }
            return View();
        }

        [HttpGet]
        public ActionResult RetrievalList()
        {
            RetrievalVM vm = new RetrievalVM()
            {
                RetrievalDictionary = new Dictionary<string, Dictionary<int, RetrievalVM.Retrieval>>(),
                StationeryRetrievalDictionary = new Dictionary<int, RetrievalVM.Retrieval>()
            };
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var retrievals = db.RequisitionDetails
                    .Where(rd => db.Requisitions.Where(r => r.Status == "Approved")
                                                .Select(r => r.RequisitionId)
                                                .ToList()
                                                .Contains(rd.RequisitionId))
                    .Join(db.Requisitions, rd => rd.RequisitionId, r => r.RequisitionId, (rd, r) => new
                    {
                        r.ApplicantId,
                        rd.StationeryId,
                        rd.Quantity
                    }).Join(db.Users, rd => rd.ApplicantId, u => u.UserId, (rd, u) => new
                    {
                        u.DeptId,
                        rd.StationeryId,
                        rd.Quantity
                    }).Join(db.Stationeries, rd => rd.StationeryId, s => s.StationeryId, (rd, s) => new
                    {
                        rd.DeptId,
                        rd.StationeryId,
                        s.Description,
                        rd.Quantity
                    }).ToList();

                var stockList = db.Stationeries.Select(s => new
                {
                    s.StationeryId,
                    s.Qty
                }).ToList();

                Dictionary<int, int> stock = new Dictionary<int, int>();
                foreach (var s in stockList)
                {
                    stock[s.StationeryId] = s.Qty;
                }

                foreach (var r in retrievals)
                {
                    String DeptName = db.Depts.Where(d => d.DeptId == r.DeptId).First().Name;
                    if (!vm.RetrievalDictionary.TryGetValue(DeptName, out Dictionary<int, RetrievalVM.Retrieval> retrievalList))
                        vm.RetrievalDictionary[DeptName] = new Dictionary<int, RetrievalVM.Retrieval>();
                    if (!vm.RetrievalDictionary[DeptName].TryGetValue(r.StationeryId, out RetrievalVM.Retrieval retrieval))
                    {
                        vm.RetrievalDictionary[DeptName][r.StationeryId] = new RetrievalVM.Retrieval()
                        {
                            DeptId = r.DeptId,
                            StationeryId = r.StationeryId,
                            Description = r.Description,
                            NeededQty = r.Quantity,
                        };
                    }
                    else
                    {
                        vm.RetrievalDictionary[DeptName][r.StationeryId].NeededQty += r.Quantity;
                    }

                    // automatically allocate
                    if (stock[r.StationeryId] >= r.Quantity)
                    {
                        vm.RetrievalDictionary[DeptName][r.StationeryId].RetrievalQty += r.Quantity;
                        stock[r.StationeryId] -= r.Quantity;
                    }
                    else
                    {
                        vm.RetrievalDictionary[DeptName][r.StationeryId].RetrievalQty += stock[r.StationeryId];
                        stock[r.StationeryId] = 0;
                    }

                }

                foreach (var s in retrievals)
                {
                    if (!vm.StationeryRetrievalDictionary.TryGetValue(s.StationeryId, out RetrievalVM.Retrieval retrieval))
                    {
                        vm.StationeryRetrievalDictionary[s.StationeryId] = new RetrievalVM.Retrieval()
                        {
                            DeptId = s.DeptId,
                            StationeryId = s.StationeryId,
                            Description = s.Description,
                            NeededQty = s.Quantity
                        };
                    }
                    else
                    {
                        retrieval.NeededQty += s.Quantity;
                    }
                }
            }

            return View(vm);
        }

        [HttpPost]
        public ActionResult RetrievalList(List<RetrievalVM.Retrieval> retrievalList)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var requisitions = db.Requisitions.Where(x => x.Status == "Approved").ToList();
                foreach(var item in requisitions)
                {
                    item.Status = "Processed";
                }
                db.SaveChanges();
                Dictionary<int, int> deptDict = new Dictionary<int, int>();
                foreach (var r in retrievalList)
                {
                    if (!deptDict.TryGetValue(r.DeptId, out int disbursementId))
                    {
                        Dept dept = db.Depts.Where(x => x.DeptId == r.DeptId).FirstOrDefault();
                        DateTime now = DateTime.Now;
                        Disbursement d = new Disbursement()
                        {
                            DeptId = r.DeptId,
                            Date = now,
                            PointId = dept.PointId,
                            RepId = dept.RepId,
                            Status = "Retrieved"
                        };
                        db.Disbursements.Add(d);
                        db.SaveChanges();
                        int DisbursementId = db.Disbursements.Where(dbm => dbm.DeptId == d.DeptId && dbm.Date == now.Date)
                            .OrderByDescending(dbm => dbm.DisbursementId)
                            .First()
                            .DisbursementId;
                        deptDict[r.DeptId] = DisbursementId;
                    }
                    DisbursementDetail dd = new DisbursementDetail()
                    {
                        StationeryId = r.StationeryId,
                        DisbursementId = deptDict[r.DeptId],
                        NeededQty = r.NeededQty,
                        RetrievalQty = r.RetrievalQty,
                        DeliveryQty = r.RetrievalQty
                    };
                    db.DisbursementDetails.Add(dd);
                    var s = db.Stationeries.Where(x => x.StationeryId == r.StationeryId).FirstOrDefault();
                    s.Qty -= r.RetrievalQty;
                    db.SaveChanges();
                }
            }
            return RedirectToAction("Disbursement");
        }

        public ActionResult Disbursement()
        {
            List<DisbursementVM2> vm = new List<DisbursementVM2>();
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                foreach (var d in db.Disbursements.ToList())
                {
                    vm.Add(new DisbursementVM2()
                    {
                        DisbursementId = d.DisbursementId,
                        DeptName = db.Depts.Where(dep => dep.DeptId == d.DeptId).First().Name,
                        PointName = db.CollectionPoints.Where(c => c.PointId == d.PointId).First().Name,
                        Date = d.Date,
                        RepName = db.Users.Where(u => u.UserId == d.RepId).First().Name,
                        Status = d.Status
                    });
                }
            }
            return View(vm);
        }
        
        public ActionResult DisbursementDetail(string id)
        {
            int idInt = Int32.Parse(id);
            List<DisbursementDetailVM> vm = new List<DisbursementDetailVM>();
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                foreach (var disbursementDetail in db.DisbursementDetails.Where(dd => dd.DisbursementId == idInt).ToList())
                {
                    vm.Add(new DisbursementDetailVM()
                    {
                        DisbursementDetailId = disbursementDetail.DisbursementDetailId,
                        StationeryDescription = db.Stationeries.Where(s => s.StationeryId == disbursementDetail.StationeryId).First().Description,
                        NeededQty = disbursementDetail.NeededQty,
                        RetrievalQty = disbursementDetail.RetrievalQty,
                        DeliveryQty = disbursementDetail.DeliveryQty,
                        Comment = disbursementDetail.Comment
                    });
                }
            }
            return View(vm);
        }

        public class DisbursementDetailVM
        {
            public int DisbursementDetailId { get; set; }
            public string StationeryDescription { get; set; }
            public int NeededQty { get; set; }
            public Nullable<int> RetrievalQty { get; set; }
            public Nullable<int> DeliveryQty { get; set; }
            public string Comment { get; set; }
        }

        public class DisbursementVM2
        {
            public int DisbursementId { get; set; }
            public string DeptName { get; set; }
            public string RepName { get; set; }
            public string PointName { get; set; }
            public System.DateTime Date { get; set; }
            public string Status { get; set; }
        }

        [HttpGet]
        public ActionResult DeliveryList()
        {
            DisbursementVM vm = new DisbursementVM()
            {
                Disbursements = new Dictionary<int?, List<DisbursementVM.Disbursement>>(),
                Depts = new Dictionary<int?, DisbursementVM.Dept>()
            };
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var disbursementDetails = db.DisbursementDetails
                    .Where(dd => db.Disbursements
                    .Where(d => d.Status == "Retrieved")
                    .Select(d => d.DisbursementId)
                    .ToList()
                    .Contains(dd.DisbursementId))
                    .Join(db.Disbursements, dd => dd.DisbursementId, d => d.DisbursementId, (dd, d) => new
                    {
                        dd.DisbursementDetailId,
                        dd.DisbursementId,
                        d.DeptId,
                        dd.StationeryId,
                        dd.NeededQty,
                        dd.RetrievalQty,
                        dd.DeliveryQty,
                        dd.Comment
                    }).Join(db.Depts, dd => dd.DeptId, d => d.DeptId, (dd, d) => new
                    {
                        dd.DisbursementDetailId,
                        dd.DisbursementId,
                        dd.DeptId,
                        d.PointId,
                        d.RepId,
                        dd.StationeryId,
                        dd.NeededQty,
                        dd.RetrievalQty,
                        dd.DeliveryQty,
                        dd.Comment
                    }).ToList();
                foreach (var dd in disbursementDetails)
                {
                    if (!vm.Disbursements.TryGetValue(dd.DeptId, out List<DisbursementVM.Disbursement> dList))
                    {
                        vm.Disbursements[dd.DeptId] = new List<DisbursementVM.Disbursement>();
                        vm.Depts[dd.DeptId] = new DisbursementVM.Dept()
                        {
                            DeptId = dd.DeptId,
                            DeptName = db.Depts.Where(d => d.DeptId == dd.DeptId).FirstOrDefault().Name,
                            Point = db.CollectionPoints.Where(cp => cp.PointId == dd.PointId).FirstOrDefault().Name,
                            RepName = db.Users.Where(u => u.UserId == dd.RepId).FirstOrDefault().Name
                        };
                    }
                    vm.Disbursements[dd.DeptId].Add(new DisbursementVM.Disbursement()
                    {
                        DisbursementDetailId = dd.DisbursementDetailId,
                        DeptId = dd.DeptId,
                        StationeryDescription = db.Stationeries.Where(s => s.StationeryId == dd.StationeryId).First().Description,
                        NeededQty = dd.NeededQty,
                        RetrievalQty = dd.RetrievalQty,
                        DeliveryQty = dd.DeliveryQty,
                        Comment = dd.Comment
                    });
                }
            }
            return View(vm);
        }
        [HttpPost]
        public ActionResult DeliveryList(List<DisbursementVM.Disbursement> dList)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                HashSet<int> DisbursementIdHashSet = new HashSet<int>();
                foreach (var d in dList)
                {
                    var DisbursementDetail = db.DisbursementDetails.Where(dd => dd.DisbursementDetailId == d.DisbursementDetailId).First();
                    DisbursementDetail.DeliveryQty = d.DeliveryQty;
                    DisbursementDetail.Comment = d.Comment;
                    DisbursementIdHashSet.Add(DisbursementDetail.DisbursementId);
                    db.SaveChanges();
                }
                foreach (int id in DisbursementIdHashSet)
                {
                    var disbursement = db.Disbursements.Where(d => d.DisbursementId == id).First();
                    disbursement.Status = "Delivered";
                    db.SaveChanges();
                }
            }
            return RedirectToAction("DeliveryList");
        }
        [HttpGet]
        public ActionResult Adjustment()
        {
            AdjustmentVM vm = new AdjustmentVM()
            {
                Stationeries = new Dictionary<string, List<Stationery>>()
            };
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var stationeries = db.Stationeries.Select(s => new
                {
                    s.StationeryId,
                    s.Category,
                    s.Description,
                    s.Qty,
                    s.UOM,
                }).ToList();
                foreach (var s in stationeries)
                {
                    if (!vm.Stationeries.TryGetValue(s.Category, out List<Stationery> sList))
                    {
                        vm.Stationeries[s.Category] = new List<Stationery>();
                    }

                    vm.Stationeries[s.Category].Add(new Stationery()
                    {
                        StationeryId = s.StationeryId,
                        Category = s.Category,
                        Description = s.Description,
                        Qty = s.Qty,
                        UOM = s.UOM
                    });
                }
            }
            return View(vm);
        }
        [HttpPost]
        public ActionResult Adjustment(List<AdjustmentDetail> ad)
        {
            var user = (User)Session["user"];
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                DateTime now = DateTime.Now;
                db.Adjustments.Add(new Adjustment()
                {
                    ClerkId = user.UserId,
                    Date = now,
                    Status = "pending",
                });
                db.SaveChanges();
                Adjustment adjustment = db.Adjustments.Where(a => a.ClerkId == user.UserId && a.Date == now.Date).OrderByDescending(a => a.AdjustmentId).First();
                double total = 0.0;
                foreach (var item in ad)
                {
                    db.AdjustmentDetails.Add(new AdjustmentDetail()
                    {
                        AdjustmentId = adjustment.AdjustmentId,
                        StationeryId = item.StationeryId,
                        Qty = item.Qty,
                        Reason = item.Reason
                    });
                    db.SaveChanges();
                    total += Math.Abs(db.Stationeries.Where(s => s.StationeryId == item.StationeryId).First().Price * item.Qty);
                }
                adjustment.Amount = total;
                db.SaveChanges();
            }
            return RedirectToAction("Adjustment");
        }

    }
}