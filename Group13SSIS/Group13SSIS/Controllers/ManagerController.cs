﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Group13SSIS.Models;
using Group13SSIS.Utility;
using PagedList;

namespace Group13SSIS.Controllers
{
    public class ManagerController : Controller
    {
        public ActionResult Index()
        {
            return View();
        }


        public ActionResult CollectionPointList()
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var collectionpointlist = db.CollectionPoints.Where(x => x.Status == "Activated").ToList();
                ViewData["points"] = collectionpointlist;
            }
            return View();
        }
        [HttpGet]
        public ActionResult CreateCollectionPoint()
        {
            return View();
        }
        [HttpPost]
        public ActionResult CreateCollectionPoint(CollectionPoint point)
        {
            if (ModelState.IsValid)
            {
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    point.Status = "Activated";
                    db.CollectionPoints.Add(point);
                    db.SaveChanges();
                    return RedirectToAction("CollectionPointList");
                }
            }
            return View();
        }
        [HttpGet]
        public ActionResult EditCollectionPoint(int id)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var point = db.CollectionPoints.Where(x => x.PointId == id).FirstOrDefault();
                return View(point);
            }
        }
        [HttpPost]
        public ActionResult EditCollectionPoint(int id, CollectionPoint Point)
        {
            if (ModelState.IsValid)
            {
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    var point = db.CollectionPoints.Where(x => x.PointId == id).FirstOrDefault();
                    point.Name = Point.Name;
                    point.Location = Point.Location;
                    db.SaveChanges();
                }
            }
            return RedirectToAction("CollectionPointList");
        }
        public ActionResult DeleteCollectionPoint(int id)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var point = db.CollectionPoints.Where(x => x.PointId == id).FirstOrDefault();
                point.Status = "Expired";
                db.SaveChanges();
            }
            return RedirectToAction("CollectionPointList");
        }


        public ActionResult StationeryList(string Category, string Search, int? page)
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
        [HttpGet]
        public ActionResult CreateStationery()
        {
            return View();
        }
        [HttpPost]
        public ActionResult CreateStationery(Stationery stationery)
        {
            if (ModelState.IsValid)
            {
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    db.Stationeries.Add(stationery);
                    db.SaveChanges();
                    return RedirectToAction("StationeryList");
                }
            }
            return View();
        }
        [HttpGet]
        public ActionResult EditStationery(int id)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var stationery = db.Stationeries.Where(x => x.StationeryId == id).FirstOrDefault();
                return View(stationery);
            }
        }
        [HttpPost]
        public ActionResult EditStationery(int id, Stationery stationery1)
        {
            if (ModelState.IsValid)
            {
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    var stationery = db.Stationeries.Where(x => x.StationeryId == id).FirstOrDefault();
                    stationery.StationeryId = stationery1.StationeryId;
                    stationery.Code = stationery1.Code;
                    stationery.Description = stationery1.Description;
                    stationery.Category = stationery1.Category;
                    stationery.Price = stationery1.Price;
                    stationery.Qty = stationery1.Qty;
                    stationery.ReorderLevel = stationery1.ReorderLevel;
                    stationery.ReorderQty = stationery1.ReorderQty;
                    stationery.UOM = stationery1.UOM;
                    db.SaveChanges();
                }
                return RedirectToAction("StationeryList");
            }
            return View();
        }
        public ActionResult DeleteStationery(int id)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var stationery = db.Stationeries.Where(x => x.StationeryId == id).FirstOrDefault();
                db.Stationeries.Remove(stationery);
                db.SaveChanges();
            }
            return RedirectToAction("StationeryList");
        }


        public ActionResult SupplierList()
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var supplierlist = db.Suppliers.ToList();
                ViewData["supplierlist"] = supplierlist;
            }
            return View();
        }
        [HttpGet]
        public ActionResult CreateSupplier()
        {
            return View();
        }
        [HttpPost]
        public ActionResult CreateSupplier(Supplier supplier)
        {
            if (ModelState.IsValid)
            {
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    db.Suppliers.Add(supplier);
                    db.SaveChanges();
                    return RedirectToAction("SupplierList");
                }
            }
            return View();
        }
        [HttpGet]
        public ActionResult EditSupplier(int id)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var supplier = db.Suppliers.Where(x => x.SupplierId == id).FirstOrDefault();
                return View(supplier);
            }
        }
        [HttpPost]
        public ActionResult EditSupplier(int id, Supplier supplier0)
        {
            if (ModelState.IsValid)
            {
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    var supplier = db.Suppliers.Where(x => x.SupplierId == id).FirstOrDefault();
                    supplier.Code = supplier0.Code;
                    supplier.Name = supplier0.Name;
                    supplier.GSTNo = supplier0.GSTNo;
                    supplier.ContactName = supplier0.ContactName;
                    supplier.PhoneNo = supplier0.PhoneNo;
                    supplier.FaxNo = supplier0.FaxNo;
                    supplier.Address = supplier0.Address;
                    db.SaveChanges();
                }
                return RedirectToAction("SupplierList");
            }
            return View(supplier0);
        }
        public ActionResult DeleteSupplier(int id)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var supplier = db.Suppliers.Where(x => x.SupplierId == id).FirstOrDefault();
                db.Suppliers.Remove(supplier);
                db.SaveChanges();
            }
            return RedirectToAction("SupplierList");
        }


        public ActionResult SupplyDetailList()
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var supplyDetailList = db.SupplyDetails.ToList();
                List<SupplyDetailVM> supplyDetailVMs = new List<SupplyDetailVM>();
                foreach (var supplyDetail in supplyDetailList)
                {
                    var supplyDetailVM = new SupplyDetailVM()
                    {
                        SupplyDetailId = supplyDetail.SupplyDetailId,
                        StationeryId = db.Stationeries.Where(x => x.StationeryId == supplyDetail.StationeryId).FirstOrDefault().Description,
                        FirstSupplierId = db.Suppliers.Where(x => x.SupplierId == supplyDetail.FirstSupplierId).FirstOrDefault().Name,
                        SecondSupplierId = db.Suppliers.Where(x => x.SupplierId == supplyDetail.SecondSupplierId).FirstOrDefault().Name,
                        ThirdSupplierId = db.Suppliers.Where(x => x.SupplierId == supplyDetail.ThirdSupplierId).FirstOrDefault().Name
                    };
                    supplyDetailVMs.Add(supplyDetailVM);
                }
                ViewData["supplydetails"] = supplyDetailVMs;
            }
            return View();
        }
        public ActionResult SelectStationery(string Category, string Search, int? page)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var cateLst = db.Stationeries.Select(x => x.Category).Distinct().ToList();
                ViewBag.Category = new SelectList(cateLst);
                var existList = db.SupplyDetails.Select(x => x.StationeryId).ToList();
                var stationeries = db.Stationeries.Where(x => !existList.Contains(x.StationeryId)).ToList();
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
        [HttpGet]
        public ActionResult CreateSupplyDetail(string stationeryId)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                int id = Int32.Parse(stationeryId);
                var stationery = db.Stationeries.Where(x => x.StationeryId == id).FirstOrDefault();
                var suppliers = db.Suppliers.ToList();
                ViewData["stationery"] = stationery;
                ViewData["suppliers"] = suppliers;
            }
            return View();
        }
        [HttpPost]
        public ActionResult CreateSupplyDetail(string stationeryId, SupplyDetailVM supplyDetailVM)
        {
            using (Group13SSISEntities dc = new Group13SSISEntities())
            {
                int id = Int32.Parse(stationeryId);
                var stationery = dc.Stationeries.Where(x => x.StationeryId == id).FirstOrDefault();
                var suppliers = dc.Suppliers.ToList();
                ViewData["stationery"] = stationery;
                ViewData["suppliers"] = suppliers;
            }
            if (ModelState.IsValid)
            {
                if(supplyDetailVM.SecondSupplierId == supplyDetailVM.FirstSupplierId
                    || supplyDetailVM.SecondSupplierId == supplyDetailVM.ThirdSupplierId
                    || supplyDetailVM.ThirdSupplierId == supplyDetailVM.FirstSupplierId)
                {
                    ModelState.AddModelError("FirstSupplierId", "These suppliers cannot be same!");
                    return View(supplyDetailVM);
                }
                SupplyDetail supply = new SupplyDetail()
                {
                    StationeryId = Int32.Parse(stationeryId),
                    FirstSupplierId = Int32.Parse(supplyDetailVM.FirstSupplierId),
                    SecondSupplierId = Int32.Parse(supplyDetailVM.SecondSupplierId),
                    ThirdSupplierId = Int32.Parse(supplyDetailVM.ThirdSupplierId)
                };
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    db.SupplyDetails.Add(supply);
                    db.SaveChanges();
                    return RedirectToAction("SupplyDetailList");
                }
            }
            return View(supplyDetailVM);
        }
        [HttpGet]
        public ActionResult EditSupplyDetail(int id)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var suppliers = db.Suppliers.ToList();
                var supplydetail = db.SupplyDetails.Where(x => x.SupplyDetailId == id).FirstOrDefault();
                var stationery = db.Stationeries.Where(x => x.StationeryId == supplydetail.StationeryId).FirstOrDefault();
                ViewData["supplydetail"] = supplydetail;
                ViewData["stationery"] = stationery;
                ViewData["suppliers"] = suppliers;
            }
            return View();
        }
        [HttpPost]
        public ActionResult EditSupplyDetail(int id, SupplyDetailVM supplyDetailVM)
        {
            using (Group13SSISEntities dc = new Group13SSISEntities())
            {
                var suppliers = dc.Suppliers.ToList();
                var supplydetail = dc.SupplyDetails.Where(x => x.SupplyDetailId == id).FirstOrDefault();
                var stationery = dc.Stationeries.Where(x => x.StationeryId == supplydetail.StationeryId).FirstOrDefault();
                ViewData["supplydetail"] = supplydetail;
                ViewData["stationery"] = stationery;
                ViewData["suppliers"] = suppliers;
            }
            if (ModelState.IsValid)
            {
                if (supplyDetailVM.SecondSupplierId == supplyDetailVM.FirstSupplierId
                    || supplyDetailVM.SecondSupplierId == supplyDetailVM.ThirdSupplierId
                    || supplyDetailVM.ThirdSupplierId == supplyDetailVM.FirstSupplierId)
                {
                    ModelState.AddModelError("FirstSupplierId", "These suppliers cannot be same!");
                    return View(supplyDetailVM);
                }
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    SupplyDetail supply = db.SupplyDetails.Where(x => x.SupplyDetailId == id).FirstOrDefault();
                    supply.FirstSupplierId = Int32.Parse(supplyDetailVM.FirstSupplierId);
                    supply.SecondSupplierId = Int32.Parse(supplyDetailVM.SecondSupplierId);
                    supply.ThirdSupplierId = Int32.Parse(supplyDetailVM.ThirdSupplierId);
                    db.SaveChanges();
                    return RedirectToAction("SupplyDetailList");
                }
            }
            return View(supplyDetailVM);
        }
        public ActionResult DeleteSupplyDetail(int id)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                SupplyDetail supply = db.SupplyDetails.Where(x => x.SupplyDetailId == id).FirstOrDefault();
                db.SupplyDetails.Remove(supply);
                db.SaveChanges();
            }
            return RedirectToAction("SupplyDetailList");
        }
    }
}